package cn.pyg.search.controller;

import cn.pyg.pojo.ItemCat;
import cn.pyg.service.ItemSearchService;
import cn.pyg.solr.SolrItem;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 搜索系统控制器
 */
@RestController
public class SearchController {

    @Reference(timeout = 1000000)
    private ItemSearchService itemSearchService;

    @PostMapping("/Search")
    public Map<String,Object> search(@RequestBody Map<String,Object> params){
        try{
            return itemSearchService.search(params);
        }catch (Exception ex) {
            return null;
        }

    }

}
