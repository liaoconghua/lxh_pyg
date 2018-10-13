package cn.pyg.manager.controller;

import cn.pyg.common.pojo.PageResult;
import cn.pyg.pojo.Specification;
import cn.pyg.pojo.TypeTemplate;
import cn.pyg.service.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模块的控制器
 */
@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {
    @Reference(timeout = 100000)
    private TypeTemplateService typeTemplateService;

    /**
     * 分页查询类型模块
     */
    @GetMapping("/findByPage")
    public PageResult findByPage(TypeTemplate typeTemplate, Integer page, Integer rows) {

        /** GET 请求中文转码 */
        if (typeTemplate != null && StringUtils.isNoneBlank(typeTemplate.getName())) {
            try {
                typeTemplate.setName(new String(typeTemplate.getName().getBytes("ISO8859-1"), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return typeTemplateService.findByPage(typeTemplate, page, rows);
    }

    /**
     * 保存或修改模版
     */
    @PostMapping("/save")
    public boolean saveOrUpdate(@RequestBody TypeTemplate typeTemplate) {
        try {
            typeTemplateService.save(typeTemplate);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改类型模版
     */
    @PostMapping("/update")
    public boolean update(@RequestBody TypeTemplate typeTemplate) {

        try {
            typeTemplateService.update(typeTemplate);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除模板
     */
    @GetMapping("/delete")
    public boolean delete(Long[] ids) {
        try {
            typeTemplateService.delete(ids);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
