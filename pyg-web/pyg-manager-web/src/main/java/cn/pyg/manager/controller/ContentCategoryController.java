package cn.pyg.manager.controller;

import cn.pyg.common.pojo.PageResult;
import cn.pyg.pojo.ContentCategory;
import cn.pyg.service.ContentCategoryService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 内容分类控制器
 */
@RestController
@RequestMapping("/contentCategory")
public class ContentCategoryController {

    @Reference(timeout = 100000)
    private ContentCategoryService contentCategoryService;

    @GetMapping("/findByPage")
    public PageResult findByPage(ContentCategory contentCategory, Integer page, Integer rows) {
        try {
            return contentCategoryService.findByPage(contentCategory, page, rows);
        } catch (Exception e) {
        }
        return null;
    }

    @PostMapping("/save")
    public boolean saveOrUpdate(@RequestBody ContentCategory contentCategory) {
        try {
            contentCategoryService.save(contentCategory);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("/delete")
    public boolean delete(Long[] ids){
        try{
            contentCategoryService.deleteAll(ids);
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    @PostMapping("/update")
    public boolean update(@RequestBody ContentCategory contentCategory){
        try{
            contentCategoryService.update(contentCategory);
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    @GetMapping("/findContentCategoryList")
    public List<ContentCategory> findContentCategoryList(){
        try {
            return contentCategoryService.findAll();
        } catch (Exception e) {
            return null;
        }
    }
}
