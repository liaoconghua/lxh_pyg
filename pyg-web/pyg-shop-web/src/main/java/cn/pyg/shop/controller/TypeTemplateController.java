package cn.pyg.shop.controller;

import cn.pyg.pojo.TypeTemplate;
import cn.pyg.service.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 模板分类控制器
 */
@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

    @Reference(timeout = 100000)
    private TypeTemplateService typeTemplateService;

    /** 根据主键ID查询模板类型 */
    @GetMapping("/findOne")
    public TypeTemplate findOne(Long id){
        try {
            return typeTemplateService.findOne(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /** 查询规格选项数据 */
    @GetMapping("/findSpecByTemplateId")
    public List<Map> findSpecByTemplateId(Long id){
        try {
            return typeTemplateService.findSpecByTemplateId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
