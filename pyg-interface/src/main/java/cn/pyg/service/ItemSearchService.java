package cn.pyg.service;

import cn.pyg.solr.SolrItem;

import java.util.List;
import java.util.Map; /**
 * 搜索系统的接口
 */
public interface ItemSearchService {

    Map<String,Object> search(Map<String, Object> params);

    /**把 SKU商品同步到索引库*/
    void saveOrUpdate(List<SolrItem> solrItems);

    /**根据商品id删除索引库中对应的数据*/
    void delete(List<Long> goodsId);
}
