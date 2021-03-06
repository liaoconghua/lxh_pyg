package cn.pyg.portal.controller;

import cn.pyg.pojo.Content;
import cn.pyg.pojo.ContentCategory;
import cn.pyg.service.ContentService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/content")
public class ContentController {

    @Reference(timeout = 10000000)
    private ContentService contentService;

    @GetMapping("findContentByCategoryId")
    public List<Content> findContentByCategoryId(Long categoryId){
        try {
            return contentService.findContentByCategoryId(categoryId);
        } catch (Exception e) {
            return null;
        }
    }
}
