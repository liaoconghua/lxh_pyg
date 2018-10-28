package cn.pyg.cart.controller;

import cn.pyg.common.util.CookieUtils;
import cn.pyg.pojo.Cart;
import cn.pyg.service.CartService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 购物车控制器
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference(timeout = 100000000)
    private CartService cartService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    /**
     * 添加SKU商品到购物车
     *
     * @param itemId 保存的数量
     * @param num    商品的id
     * @return boolean
     */
    @GetMapping("/addCart")
    public boolean addCart(Long itemId, Integer num) {
        try {
            // 获取登录名
            String username = request.getRemoteUser();

            // 获取购物车集合
            List<Cart> carts = findCart();
            // 调用服务层添加SKU商品到购物车
            carts = cartService.addItemToCart(carts, itemId, num);

            // 判断是否登录
            if (StringUtils.isNoneBlank(username)) { // 登录
                /**  ============将购物车保存到Redis中==============  */
                cartService.saveCartRedis(username, carts);
            } else { // 未登录
                /**  ============将购物车保存到Cookie中==============  */
                // 将购物车重新载入Cookie中
                CookieUtils.setCookie(request, response,
                        CookieUtils.CookieName.PINYOUGOU_CART, JSON.toJSONString(carts),
                        3600 * 24, true);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取购物车集合
     *
     * @return List<Cart>返回多个商品集合
     */
    @GetMapping("/findCart")
    private List<Cart> findCart() {
        // 获取登录用户名
        String username = request.getRemoteUser();
        // 定义购物车集合
        List<Cart> carts = null;
        // 判断是否登录成功
        if (StringUtils.isNoneBlank(username)) { // 登录
            /**  ============从Redis中获取购物车==============  */
            carts = cartService.findCartRedis(username);

            // 从Cookie中获取购物车集合Json字符串
            String carStr = CookieUtils.getCookieValue(request, CookieUtils.CookieName.PINYOUGOU_CART
                    , true);
            // 判断是否为空
            if (StringUtils.isNoneBlank(carStr)){
                // 转化为List集合
                List<Cart> cookieCarts = JSON.parseArray(carStr, Cart.class);
                if (cookieCarts != null && cookieCarts.size() > 0){
                    // 合并购物车
                   carts =  cartService.mergeCart(cookieCarts,carts);
                }



            }

        } else { // 未登录
            /**  ============从Cookie中获取购物车==============  */
            // 从Cookie中获取购物车集合Json字符串
            String cartStr = CookieUtils.getCookieValue(request,
                    CookieUtils.CookieName.PINYOUGOU_CART, true);
            // 判断是否为空
            if (StringUtils.isBlank(cartStr)) {
                cartStr = "[]";  // 预防是null 降低报错可能
            }
            carts = JSON.parseArray(cartStr, Cart.class);
        }
        return carts;
    }
}
