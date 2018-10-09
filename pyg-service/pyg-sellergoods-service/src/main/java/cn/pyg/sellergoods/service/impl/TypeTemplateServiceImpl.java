package cn.pyg.sellergoods.service.impl;

import cn.pyg.common.pojo.PageResult;
import cn.pyg.mapper.TypeTemplateMapper;
import cn.pyg.pojo.TypeTemplate;
import cn.pyg.service.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Service(interfaceName = "cn.pyg.service.TypeTemplateService")
@Transactional
public class TypeTemplateServiceImpl implements TypeTemplateService {

    @Autowired
    private TypeTemplateMapper typeTemplateMapper;

    /** 添加类型模版 */
    @Override
    public void save(TypeTemplate typeTemplate) {
        typeTemplateMapper.insertSelective(typeTemplate);
    }

    /** 修改模版 */
    @Override
    public void update(TypeTemplate typeTemplate) {
        typeTemplateMapper.updateByPrimaryKeySelective(typeTemplate);
    }


    @Override
    public void delete(Serializable id) {

    }

    /** 删除模版 */
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
        return null;
    }

    @Override
    public List<TypeTemplate> findAll() {
        return null;
    }

    /** 分页查询模块列表 */
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
            return new PageResult(pageInfo.getTotal(),pageInfo.getList());
        } catch (Exception e) {
                throw new RuntimeException(e);
        }
    }
}
