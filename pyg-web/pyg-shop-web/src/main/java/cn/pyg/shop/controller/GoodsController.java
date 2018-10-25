package cn.pyg.shop.controller;

import cn.pyg.common.pojo.PageResult;
import cn.pyg.pojo.Goods;
import cn.pyg.service.GoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * 新增商品控制器
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference(timeout = 10000000)
    private GoodsService goodsService;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination solrQueue;
    @Autowired
    private Destination solrDeleteQueue;
    @Autowired
    private Destination pageTopic;
    @Autowired
    private Destination deleteTopic;

    @PostMapping("/save")
    public boolean save(@RequestBody Goods goods) {
        try {
            // 获取商家的id (登录名)
            String selectId = SecurityContextHolder.getContext().getAuthentication().getName();
            goods.setSellerId(selectId);
            goodsService.save(goods);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("/findByPage")
    public PageResult findByPage(Goods goods, Integer page, Integer rows) {
        // 因为只查询当前登录用户的数据, 所以需要获取到当前的用户ID
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        // 添加查询条件
        goods.setSellerId(sellerId);
        // GET请求中文转码
        if (goods.getGoodsName() != null
                && StringUtils.isNoneBlank(goods.getGoodsName())) {
            try {
                goods.setGoodsName(new String(goods.getGoodsName()
                        .getBytes("ISO8859-1"), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return goodsService.findByPage(goods, page, rows);
    }

    /** 商品上下架 */
    @GetMapping("/updateMarketable")
    public boolean updateMarketable(Long[] ids, String status){
        try{

            goodsService.updateMarketable(ids,status);
            // 判断商品上下架状态
            if ("1".equals(status)){  // 表示上架
                /** 发送消息,生成商品索引 */
                jmsTemplate.send(solrQueue,new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createObjectMessage(ids);
                    }
                });

                /** 发送消息,生成静态网页 */
                for (Long goodsId : ids) {
                    jmsTemplate.send(pageTopic, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            return session.createTextMessage(goodsId.toString());
                        }
                    });

                }
            }else {  // 表示下架
                // 发送消息,生成商品索引
                jmsTemplate.send(solrDeleteQueue,new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createObjectMessage(ids);
                    }
                });

                /** 发送消息,删除静态网页 */
                jmsTemplate.send(deleteTopic, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createObjectMessage(ids);
                    }
                });

            }
            return true;
        }catch (Exception ex){
          ex.printStackTrace();
        }
        return false;
    }

    /** 根据商品id进行删除 */
    @GetMapping("/delete")
    public boolean delete(Long[] ids){
        try{
            goodsService.updateDeleteStatus(ids);
            return  true;
        }catch (Exception ex){
            return false;
        }
    }

}
