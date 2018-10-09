package cn.pyg.mapper;

import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import cn.pyg.pojo.Specification;

import java.util.List;
import java.util.Map;

/**
 * SpecificationMapper 数据访问接口
 *
 * @version 1.0
 * @date 2018-09-28 08:31:46
 */
public interface SpecificationMapper extends Mapper<Specification> {

    /** 自定义查询所有规格列表 */
    List<Specification> findAll(Specification specification);

    /** 查询所有的规格 */
    @Select("select id, spec_name as text from tb_specification order by id asc ")
    List<Map<String,Object>> findAllByIdAndName();
}