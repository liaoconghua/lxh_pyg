package cn.pyg.mapper;

import cn.pyg.pojo.Brand;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 品牌的数据访问层接口
 */
public interface BrandMapper {

    /** 查询全部品牌 */
    @Select("SELECT * FROM tb_brand ORDER BY id ASC ")
    List<Brand> findAll();
}
