package cn.pyg.item.controller;

import cn.pyg.service.GoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class ItemController {

    @Reference(timeout = 100000000)
    private GoodsService goodsService;

    @GetMapping("/{goodsId}")
    public String getGoods(@PathVariable Long goodsId, Model model){
        Map<String, Object> data = goodsService.getGoods(goodsId);


        model.addAllAttributes(data);
        // 返回的是freemarker模板页面
        return "item";

    }
}
