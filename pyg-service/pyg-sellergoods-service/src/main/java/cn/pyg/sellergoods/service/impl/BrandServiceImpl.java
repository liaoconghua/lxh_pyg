package cn.pyg.sellergoods.service.impl;

import cn.pyg.mapper.BrandMapper;
import cn.pyg.pojo.Brand;
import cn.pyg.service.BrandService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * 查询所有的品牌列表
     * @return
     */
    @Override
    public List<Brand> findAllBrand() {
        return brandMapper.findAll();
    }
}
