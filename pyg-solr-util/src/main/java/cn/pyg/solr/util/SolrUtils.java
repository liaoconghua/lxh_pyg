package cn.pyg.solr.util;

import cn.pyg.mapper.ItemMapper;
import cn.pyg.pojo.Item;
import cn.pyg.solr.SolrItem;
import com.alibaba.fastjson.JSON;
import javafx.application.Application;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * SolrUtils
 */
@Component
public class SolrUtils {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrTemplate solrTemplate;

    /** 导入商品数据 */

    public void importItemData(){
        // 创建item对象封建查询条件
        Item item = new Item();
        // 正常的商品
        item.setStatus("1");
        // 从数据表中查询商品数据
        List<Item> itemList = itemMapper.select(item);
        // 定义SolrItem集合封装文档数据
        List<SolrItem> solrItems = new ArrayList<>();

        System.out.println("===商品列表===");
        // 循环将List<Item> 转化为 List<SolrItem>
        for (Item item1 : itemList) {
            SolrItem solrItem = new SolrItem();
            solrItem.setId(item1.getId());
            solrItem.setTitle(item1.getTitle());
            solrItem.setPrice(item1.getPrice());
            solrItem.setImage(item1.getImage());
            solrItem.setGoodsId(item1.getGoodsId());
            solrItem.setBrand(item1.getBrand());
            solrItem.setSeller(item1.getSeller());
            solrItem.setUpdateTime(item1.getUpdateTime());

            // tb_item  spec: {"网络":"移动4G","机身内存":"64G"}
            // 把json字符串转化成Map集合
            Map<String,String> specMap = JSON.parseObject(item1.getSpec(), Map.class);
            // 设置动态域
            solrItem.setSpecMap(specMap);

            solrItems.add(solrItem);

        }

        // 添加或修改索引库
        UpdateResponse updateResponse = solrTemplate.saveBeans(solrItems);
        if (updateResponse.getStatus() == 0){
            solrTemplate.commit();
        }else {
            solrTemplate.rollback();
        }
        System.out.println("===结束===");
    }

    // 调用以上的方法
    public static void main(String[] args){
        // 获取Spring容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 获取SolrUtils
        SolrUtils solrUtils = ac.getBean(SolrUtils.class);
        solrUtils.importItemData();
    }
}
