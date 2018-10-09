package cn.pyg.sellergoods.service.impl;

import cn.pyg.common.pojo.PageResult;
import cn.pyg.mapper.BrandMapper;
import cn.pyg.pojo.Brand;
import cn.pyg.service.BrandService;
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
import java.util.Map;

/**
 * 品牌业务层的实现类
 *
 *  Service:    dubbo的service 找到对应的接口
 *
 */
@Service(interfaceName = "cn.pyg.service.BrandService")
@Transactional
public class BrandServiceImpl implements BrandService {

    /** 注入数据访问接口的代理对象 */
    @Autowired
    private BrandMapper brandMapper;

    /** 添加品牌 */
    @Override
    public void save(Brand brand) {
        brandMapper.insertSelective(brand);
    }

    /** 修改品牌 */
    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKey(brand);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Brand findOne(Long id) {
        return null;
    }

    @Override
    public List<Brand> findAll() {
        return null;
    }

    /** 分页查询品牌 */
    @Override
    public PageResult findByPage(Brand brand, int page, int rows) {
        try {
            PageInfo<Brand> pageInfo = PageHelper.startPage(page, rows).
                    doSelectPageInfo(new ISelect() {
                @Override
                public void doSelect() {
                    brandMapper.findAll(brand);
                }
            });
            return new PageResult(pageInfo.getTotal(),pageInfo.getList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /** 删除品牌 */
    @Override
    public void deleteAll(Long[] ids) {
        // 创建示范对象
        Example ex = new Example(Brand.class);
        // 创建条件对象
        Example.Criteria criteria = ex.createCriteria();
        // 添加in条件
        criteria.andIn("id", Arrays.asList(ids));
        // 根据条件删除
        brandMapper.deleteByExample(ex);
    }

    /** 查询所有的品牌 */
    @Override
    public List<Map<String, Object>> findAllByIdAndName() {
        try {
            return  brandMapper.findAllByIdAndName();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
