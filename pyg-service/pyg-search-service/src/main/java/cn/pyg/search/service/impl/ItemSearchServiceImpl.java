package cn.pyg.search.service.impl;

import cn.pyg.service.ItemSearchService;
import cn.pyg.solr.SolrItem;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import sun.java2d.pipe.SpanShapeRenderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceName = "cn.pyg.service.ItemSearchService")
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    // 搜索方法
    @Override
    public Map<String, Object> search(Map<String, Object> params) {

        // 创建Map集合封装返回的数据
        Map<String, Object> data = new HashMap<>();
        // 获取检查关键字
        String keywords = (String) params.get("keywords");
        /** 获取当前页码 */
        Integer page = (Integer) params.get("page");
        if (page == null){
            // 默认第一页
            page = 1;
        }
        /** 获取每页显示的记录数 */
        Integer rows = (Integer) params.get("rows");
        if (rows == null){
            // 默认20条记录
            rows = 20;
        }
        // 判断检索关键字是否有
        if (StringUtils.isNoneBlank(keywords)) {

            // 创建高亮查询对象
            HighlightQuery highlightQuery = new SimpleHighlightQuery();
            // 创建高亮选择对象
            HighlightOptions highlightOptions = new HighlightOptions();
            // 设置高亮域
            highlightOptions.addField("title");
            // 设置高亮前缀
            highlightOptions.setSimplePrefix("<font color='red'>");
            // 设置高亮后缀
            highlightOptions.setSimplePostfix("</font>");
            // 设置高亮选项
            highlightQuery.setHighlightOptions(highlightOptions);
            // 创建查询条件
            Criteria criteria = new Criteria("keywords").is(keywords);
            // 添加查询条件
            highlightQuery.addCriteria(criteria);

            /** 按商品分类过滤 */
            if (!"".equals(params.get("category"))) { // 不为空
                Criteria criteria1 = new Criteria("category").is(params.get("category"));// 找到file域中的category 是 params中的这个category
                // 添加过滤条件
                highlightQuery.addFilterQuery(new SimpleFilterQuery(criteria1));
            }
            /** 按品牌过滤 */
            if (!"".equals(params.get("brand"))){ // 不为空
                Criteria criteria1 = new Criteria("brand").is(params.get("brand"));
                // 添加过滤方法
                highlightQuery.addFilterQuery(new SimpleFilterQuery(criteria1));
            }
            /** 根据规格过滤 */
            if (params.get("spec") != null){
                Map<String,String> specMap = (Map)params.get("spec");
                for (String key : specMap.keySet()) {
                    Criteria criteria1 = new Criteria("spec_" + key).is(specMap.get(key));
                    // 添加过滤条件
                    highlightQuery.addFilterQuery(new SimpleFilterQuery(criteria1));
                }
            }
            /** 根据价格过滤 */
            if (!"".equals(params.get("price"))){
                // 得到价格范围数组
                String [] price = params.get("price").toString().split("-");
                // 如果价格区间起点不等于零
                if (!price[0].equals("0")){
                    Criteria criteria1 = new Criteria("price").greaterThanEqual(price[0]);
                    highlightQuery.addFilterQuery(new SimpleFilterQuery(criteria1));
                }
                // 如果价格区间终点不等于*
                if (!price[1].equals("*")){
                    Criteria criteria1 = new Criteria("price").lessThanEqual(price[1]);
                    highlightQuery.addFilterQuery(new SimpleFilterQuery(criteria1));
                }
            }

            /** 设置起始记录查询数 */
            highlightQuery.setOffset((page - 1) * rows);
            /** 设置每页显示记录数 */
            highlightQuery.setRows(rows);


            /** 高亮分页查询对象 */
            HighlightPage<SolrItem> highlightPage = solrTemplate
                    .queryForHighlightPage(highlightQuery, SolrItem.class);
            // 循环高亮项集合
            for (HighlightEntry<SolrItem> he : highlightPage.getHighlighted()) {
                // 获取检索到的原实体
                SolrItem solrItem = he.getEntity();
                // 判断高亮集合及集合中的第一个Field的高亮内容
                if (he.getHighlights().size() > 0
                        && he.getHighlights().get(0).getSnipplets().size() > 0) {
                    // 设置高亮的结果
                    solrItem.setTitle((he.getHighlights().get(0).getSnipplets().get(0)));
                }
            }
            data.put("rows", highlightPage.getContent());
            /** 设置总页数 */
            data.put("totalPages", highlightPage.getTotalPages());
            /** 设置总记录数 */
            data.put("total", highlightPage.getTotalElements());

        } else {   // 简单查询
            SimpleQuery simpleQuery = new SimpleQuery("*:*");
            /** 设置起始记录查询数 */
            simpleQuery.setOffset(page);
            /** 设置每页显示记录数 */
            simpleQuery.setRows(rows);
            // 分页检索
            ScoredPage<SolrItem> scoredPage = solrTemplate.queryForPage(simpleQuery, SolrItem.class);
            // 获取内容
            data.put("rows", scoredPage.getContent());
            /** 设置起始记录数 */
            data.put("totalPages", scoredPage.getTotalPages());
            /** 设置总记录数 */
            data.put("total", scoredPage.getTotalElements());


        }
        return data;
    }

    /** 把 SKU商品同步到索引库 */
    @Override
    public void saveOrUpdate(List<SolrItem> solrItems) {
        UpdateResponse updateResponse = solrTemplate.saveBeans(solrItems);
        // 做操作成功或操作失败的处理
        if (updateResponse.getStatus() == 0){
            solrTemplate.commit();
        }else {
            solrTemplate.rollback();
        }
    }

    /** 根据商品id删除索引库中对应的数据 */
    @Override
    public void delete(List<Long> goodsId) {
        Query query = new SimpleQuery();
        Criteria criteria1 = new Criteria("goodsId").in(goodsId);
        query.addCriteria(criteria1);
        UpdateResponse updateResponse = solrTemplate.delete(query);
        if (updateResponse.getStatus() == 0){
            solrTemplate.commit();
        }else {
            solrTemplate.rollback();
        }
    }
}
