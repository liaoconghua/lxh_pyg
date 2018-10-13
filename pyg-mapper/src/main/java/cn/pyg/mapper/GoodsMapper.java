package cn.pyg.mapper;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import cn.pyg.pojo.Goods;

import java.util.List;
import java.util.Map;

/**
 * GoodsMapper 数据访问接口
 * @date 2018-09-28 08:31:46
 * @version 1.0
 */
public interface GoodsMapper extends Mapper<Goods>{


    /** 多条件查询 */
    List<Map<String,Object>> findAll(Goods goods);

    /** 修改商品的状态 */
    void updateStatus(@Param("ids") Long[] ids,
                      @Param("status") String status,
                      @Param("columnName") String columnName);
}