package cn.pyg.manager.controller;

import cn.pyg.pojo.Brand;
import cn.pyg.service.BrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 品牌的表现层
 */
@RestController
public class BrandController {

    /**
     * 引用服务接口,产生服务接口的代理对象
     * timeout: 调用服务接口方法超时时间的毫秒值
     */
    @Reference(timeout = 10000)
    private BrandService brandService;

    /** 查询全部品牌 */
    @GetMapping("/brand/findAll")
    public List<Brand> findAll(){
        return brandService.findAllBrand();
    }

}
