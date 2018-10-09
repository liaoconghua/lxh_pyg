package cn.pyg.mapper;

import cn.pyg.pojo.TypeTemplate;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


/**
 * TypeTemplateMapper 数据访问接口
 * @date 2018-09-28 08:31:46
 * @version 1.0
 */
public interface TypeTemplateMapper extends Mapper<TypeTemplate> {

    /** 分页查询列表数据 */
    List<TypeTemplate> findAll(TypeTemplate typeTemplate);
}