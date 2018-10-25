package cn.pyg.search.listener;
import java.math.BigDecimal;
import java.util.*;

import cn.pyg.pojo.Goods;
import cn.pyg.pojo.Item;
import cn.pyg.service.GoodsService;
import cn.pyg.service.ItemSearchService;
import cn.pyg.solr.SolrItem;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.SessionAwareMessageListener;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.Serializable;

/**
 * 商品消息监听器
 *
 */
public class ItemMessageListener implements SessionAwareMessageListener<ObjectMessage>{

    @Reference(timeout = 30000000)
    private GoodsService goodsService;
    @Reference(timeout = 30000000)
    private ItemSearchService itemSearchService;



    @Override
    public void onMessage(ObjectMessage objectMessage, Session session) throws JMSException {
        System.out.println("==ItemMessageListener==");

        // 获取消息内容
        Long[] ids = (Long[]) objectMessage.getObject();
        System.out.println("ids: " + Arrays.toString(ids));

        // 根据商家id去查询
        List<Item> itemList =  goodsService.findItemByGoodId(ids);

        // 判断集合
        if (itemList.size() > 0){
            // 把List<Item>转化成List<SolrItem>
            List<SolrItem> solrItems = new ArrayList<>();
            for (Item item : itemList) {
            SolrItem solrItem = new SolrItem();
            solrItem.setTitle(item.getTitle());
            solrItem.setSpecMap(JSON
                    .parseObject(item.getSpec(),Map.class));
            solrItem.setUpdateTime(item.getUpdateTime());
            solrItem.setSeller(item.getSeller());
            solrItem.setPrice(item.getPrice());
            solrItem.setImage(item.getImage());
            solrItem.setGoodsId(item.getGoodsId());
            solrItem.setCategory(item.getCategory());
            solrItem.setBrand(item.getBrand());
            solrItem.setId(item.getId());

            solrItems.add(solrItem);
            }
            // 把SKU商品数据同步到索引库
            itemSearchService.saveOrUpdate(solrItems);
        }

    }
}
