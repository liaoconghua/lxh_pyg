package cn.pyg.service;

import cn.pyg.pojo.Cart;

import java.util.List;

/** 购物车服务接口 */
public interface CartService {

    /**
     * @param carts
     * @param itemId
     * @param num
     * @return
     */
    List<Cart> addItemToCart(List<Cart> carts, Long itemId, Integer num);

    /**
     * 从Redis获取购物车
     * @param username
     * @return
     */
    List<Cart> findCartRedis(String username);

    /**
     *
     * @param username
     * @param carts
     */
    void saveCartRedis(String username, List<Cart> carts);

    /**
     * 合并购物车
     * @param cookieCarts   cookie购物车
     * @param redisCarts    redis购物车
     * @return
     */
    List<Cart> mergeCart(List<Cart> cookieCarts, List<Cart> redisCarts);
}
