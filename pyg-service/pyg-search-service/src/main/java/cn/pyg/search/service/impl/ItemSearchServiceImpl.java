package cn.pyg.search.service.impl;

import cn.pyg.service.ItemSearchService;
import cn.pyg.solr.SolrItem;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service(interfaceName = "cn.pyg.service.ItemSearchService")
@Transactional
public class ItemSearchServiceImpl implements ItemSearchService{

    @Autowired
    private SolrTemplate solrTemplate;

    // 搜索方法
    @Override
    public Map<String, Object> search(Map<String, Object> params) {

        // 创建Map集合封装返回的数据
        Map<String,Object> data = new HashMap<>();
        // 创建查询对象
        Query query = new SimpleQuery("*:*");
        // 获取检查关键字
        String keywords = (String) params.get("keywords");
        // 判断检索关键字是否有
        if (StringUtils.isNoneBlank(keywords)){
            // 创建查询条件
            Criteria criteria = new Criteria("keywords").is(keywords);
            // 添加查询条件
            query.addCriteria(criteria);
        }
        // 分页检索
        ScoredPage<SolrItem> scoredPage = solrTemplate.queryForPage(query, SolrItem.class);
        // 获取内容
        data.put("rows", scoredPage.getContent());

        return data;
    }
}
