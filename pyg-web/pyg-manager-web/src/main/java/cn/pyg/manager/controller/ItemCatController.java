package cn.pyg.manager.controller;

import cn.pyg.common.pojo.PageResult;
import cn.pyg.pojo.ItemCat;
import cn.pyg.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品分类
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference(timeout = 10000)
    private ItemCatService itemCatService;

    /**
     * 根据商品id分类查询
     */
    @GetMapping("/findItemCatByParentId")
    public List<ItemCat> findItemCatByParentId(@RequestParam Long parentId) {
        return itemCatService.findItemCatByParentId(parentId);
    }
}
