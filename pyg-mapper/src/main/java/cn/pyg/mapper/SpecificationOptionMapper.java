package cn.pyg.mapper;

import cn.pyg.pojo.Specification;
import tk.mybatis.mapper.common.Mapper;

import cn.pyg.pojo.SpecificationOption;

/**
 * SpecificationOptionMapper 数据访问接口
 * @date 2018-09-28 08:31:46
 * @version 1.0
 */
public interface SpecificationOptionMapper extends Mapper<SpecificationOption>{

    /** 添加规格选项 */
    void save(Specification specification);
}