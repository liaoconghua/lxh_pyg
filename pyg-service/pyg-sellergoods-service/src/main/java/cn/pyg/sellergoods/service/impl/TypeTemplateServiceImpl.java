package cn.pyg.sellergoods.service.impl;

import cn.pyg.common.pojo.PageResult;
import cn.pyg.mapper.SpecificationOptionMapper;
import cn.pyg.mapper.TypeTemplateMapper;
import cn.pyg.pojo.SpecificationOption;
import cn.pyg.pojo.TypeTemplate;
import cn.pyg.service.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service(interfaceName = "cn.pyg.service.TypeTemplateService")
@Transactional
public class TypeTemplateServiceImpl implements TypeTemplateService {

    @Autowired
    private TypeTemplateMapper typeTemplateMapper;
    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;

    /**
     * 添加类型模版
     */
    @Override
    public void save(TypeTemplate typeTemplate) {
        typeTemplateMapper.insertSelective(typeTemplate);
    }

    /**
     * 修改模版
     */
    @Override
    public void update(TypeTemplate typeTemplate) {
        typeTemplateMapper.updateByPrimaryKeySelective(typeTemplate);
    }


    @Override
    public void delete(Serializable id) {

    }

    /**
     * 删除模版
     */
    @Override
    public void deleteAll(Serializable[] ids) {
        // 创建示范对象
        Example example = new Example(TypeTemplate.class);
        // 创建条件对象
        Example.Criteria criteria = example.createCriteria();
        // 添加in条件
        criteria.andIn("id", Arrays.asList(ids));
        // 条件删除
        typeTemplateMapper.deleteByExample(example);
    }

    @Override
    public TypeTemplate findOne(Serializable id) {
        return typeTemplateMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TypeTemplate> findAll() {
        return null;
    }

    /**
     * 分页查询模块列表
     */
    @Override
    public PageResult findByPage(TypeTemplate typeTemplate, int page, int rows) {
        try {
            // 开启分页
            PageInfo<TypeTemplate> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
                @Override
                public void doSelect() {
                    typeTemplateMapper.findAll(typeTemplate);
                }
            });
            return new PageResult(pageInfo.getTotal(), pageInfo.getList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询规格选项数据
     */
    @Override
    public List<Map> findSpecByTemplateId(Long id) {
        /**
         * [{"id":27,"text":"网络","options":[{},{}]}
         *      ,{"id":32,"text":"机身内存","options":[{},{}]}]
         */
        // 根据id查询类型模版对象
        TypeTemplate typeTemplate = findOne(id);
        // 或规格数据
        // [{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        String specIds = typeTemplate.getSpecIds();
        // 把String字符串转换为List<Map>
        // JSON.parseArray() [{},{}]
        // JSON.parseObject() {}
        List<Map> specMaps = JSON.parseArray(specIds, Map.class);
        // 迭代集合
        for (Map specMap : specMaps) {
            // map: {"id":27,"test":"网络"}
            // 把id转换integer类型 以防报错
            Integer specId = (Integer) specMap.get("id");
            // 需要一条查询语句
            // select * from tb_specification_option where spac_id = ?
            // 创建SpecificationOption对象封装条件
            SpecificationOption specificationOption = new SpecificationOption();
            specificationOption.setSpecId(specId.longValue());
            // 根据规格id查询规格选项
            List<SpecificationOption> options = specificationOptionMapper.select(specificationOption);
            // 向map中添加这个key
            specMap.put("options", options);
        }

        return specMaps;
    }
}
