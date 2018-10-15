package cn.pyg.manager.controller;

import cn.pyg.common.pojo.PageResult;
import cn.pyg.pojo.Content;
import cn.pyg.service.ContentService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.ContentModel;

/**
 * 广告表
 */
@RestController
@RequestMapping("/content")
public class ContentController {

    @Reference(timeout = 10000000)
    private ContentService contentService;


    @GetMapping("/findByPage")
    public PageResult findByPage(Content content, Integer page, Integer rows) {
        try {
            return contentService.findByPage(content, page, rows);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/save")
    public boolean save(@RequestBody Content content) {
        try {
            contentService.save(content);
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

    @PostMapping("/update")
    public boolean update(@RequestBody Content content, Integer categoryId) {
        try {
            contentService.update(content);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @GetMapping("/delete")
    public boolean delete(Long[] ids){
        try{
            contentService.deleteAll(ids);
            return true;
        }catch (Exception ex){
            return false;
        }
    }
}
