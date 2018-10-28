package cn.pyg.cart.service.impl;

import cn.pyg.mapper.ItemMapper;
import cn.pyg.pojo.Cart;
import cn.pyg.pojo.Item;
import cn.pyg.pojo.Order;
import cn.pyg.pojo.OrderItem;
import cn.pyg.service.CartService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车服务接口实现类
 */
@Service(interfaceName = "cn.pyg.service.CartService")
public class CartServiceImpl implements CartService {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /***
     * 添加SKU商品到购物车
     * @param carts     购物车集合
     * @param itemId    商品id
     * @param num       数量
     * @return
     */
    @Override
    public List<Cart> addItemToCart(List<Cart> carts, Long itemId, Integer num) {

        try {
            //1.根据SKU商品ID查询SKU商品对象
            Item item = itemMapper.selectByPrimaryKey(itemId);
            //2.获取商家ID
            String sellerId = item.getSellerId();
            //3.根据商家ID判断购物车集合中是否存在该商家的购物车
            Cart cart = searchCartBySellerId(carts, sellerId);
            //4.如果购物车集合中不存在该商家的购物车
            if (cart == null) {
                //4.1 创建新的购物车对象
                cart = new Cart();
                cart.setSellerId(sellerId);
                cart.setSellerName(item.getSeller());
                // 创建订单明细
                OrderItem orderItem = createOrderItem(item, num);
                List<OrderItem> orderItems = new ArrayList<>();
                orderItems.add(orderItem);
                // 为购物车设置订单明细集合
                cart.setOrderItems(orderItems);
                //4.2 将新的购物车对象添加到购物车集合
                carts.add(cart);
            } else {
                //5.如果购物车集合中存在该商家的购物车
                OrderItem orderItem = searchOrderItemId(cart.getOrderItems(), itemId);
                //5.1 判断购物车订单明细集合中是否存在该商品
                if (orderItem == null){
                    // 没有, 就往里面添加
                    orderItem = createOrderItem(item, num);
                    cart.getOrderItems().add(orderItem);
                }else {
                    // 有,就在原购物车订单明细上添加数量,更改金额
                    orderItem.setNum(orderItem.getNum() + num);
                    orderItem.setTotalFee(new BigDecimal(
                            orderItem.getPrice().doubleValue()
                                    * orderItem.getNum()));
                    // 如果订单购买数小于等于0,则删除
                    if (orderItem.getNum() <= 0){
                        // 删除购物车中的订单明细(商品)
                        cart.getOrderItems().remove(orderItem);
                    }

                    // 如果cart的orderItems订单明细为0,则删除cart
                    if (cart.getOrderItems().size() == 0){
                        carts.remove(cart);
                    }
                }
            }
            return carts;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从Redis中获取购物车
     * @param username
     * @return
     */
    @Override
    public List<Cart> findCartRedis(String username) {
        System.out.println("======================---Redis---=======================");

        List<Cart> carts = (List<Cart>) redisTemplate.boundValueOps("cart_" + username).get();
        if (carts == null){
            carts = new ArrayList<>(0);
        }
        return carts;
    }

    /**
     * 将购物车保存到Redis中
     * @param username
     * @param carts
     */
    @Override
    public void saveCartRedis(String username, List<Cart> carts) {
        System.out.println("往Redis存入购物车");
        redisTemplate.boundValueOps("cart_" + username).set(carts);
    }

    /**
     * 合并购物车
     * @param cookieCarts   cookie购物车
     * @param redisCarts    redis购物车
     * @return
     */
    @Override
    public List<Cart> mergeCart(List<Cart> cookieCarts, List<Cart> redisCarts) {
        // 迭代Cookie中的购物车数据
        for (Cart cart : cookieCarts) {
            // 迭代订单明细
            for (OrderItem orderItem : cart.getOrderItems()) {
                redisCarts = addItemToCart(redisCarts, orderItem.getItemId(), orderItem.getNum());
            }
        }
        return redisCarts;
    }


    /**
     * 判断购物车集合中存在该商家的购物车
     *
     * @param orderItems
     * @param itemId
     */
    private OrderItem searchOrderItemId(List<OrderItem> orderItems, Long itemId) {
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getItemId().equals(itemId)) {
                return orderItem;
            }
        }
        return null;

    }

    /**
     * 创建订单明细
     *
     * @param item SKU数据
     * @param num  数量
     * @return
     */
    private OrderItem createOrderItem(Item item, Integer num) {
        // 创建订单明细
        OrderItem orderItem = new OrderItem();
        orderItem.setSellerId(item.getSellerId());
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setItemId(item.getId());
        orderItem.setNum(num);
        orderItem.setPicPath(item.getImage());
        orderItem.setPrice(item.getPrice());
        orderItem.setTitle(item.getTitle());

        // 小计
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue() * num));

        return orderItem;
    }

    /**
     * 根据商家ID判断购物车集合中是否存在该商家的购物车
     *
     * @param carts    购物车集合
     * @param sellerId 商品id
     * @return 购物车
     */
    private Cart searchCartBySellerId(List<Cart> carts, String sellerId) {
        for (Cart cart : carts) {
            if (cart.getSellerId().equals(sellerId)) {
                return cart;
            }
        }
        return null;
    }
}
