package cn.pyg.shop.controller;

import cn.pyg.pojo.ItemCat;
import cn.pyg.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品分类控制层
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference(timeout = 100000)
    private ItemCatService itemCatService;

    @GetMapping("/findItemCatByParentId")
    public List<ItemCat> findItemCatByParentId(Long parentId) {
        return itemCatService.findItemCatByParentId(parentId);

    }

}
