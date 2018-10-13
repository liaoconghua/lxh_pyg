package cn.pyg.shop.controller;

import cn.pyg.common.pojo.PageResult;
import cn.pyg.pojo.Goods;
import cn.pyg.service.GoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Security;

/**
 * 新增商品控制器
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference(timeout = 100000)
    private GoodsService goodsService;

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
}
