package cn.pyg.service;

import cn.pyg.pojo.Brand;

import java.util.List;

/**
 * 品牌的业务层接口
 */
public interface BrandService {

    /** 查询所有的品牌 */
    List<Brand> findAllBrand();

}
