package cn.pyg.manager.controller;

import cn.pyg.common.pojo.PageResult;
import cn.pyg.pojo.Brand;
import cn.pyg.service.BrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * 品牌的表现层
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    /**
     * 引用服务接口,产生服务接口的代理对象
     * timeout: 调用服务接口方法超时时间的毫秒值
     */
    @Reference(timeout = 100000)
    private BrandService brandService;

    /** 查询全部品牌 */
    @GetMapping("/findByPage")
    public PageResult findAll(Brand brand, Integer page, Integer rows){
        if (brand != null && StringUtils.isNoneBlank(brand.getName())){
            try {
                brand.setName(new String(brand.getName()
                        .getBytes("ISO8859-1"),"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return brandService.findByPage(brand,page,rows) ;
    }

    /** 保存品牌 */
    @PostMapping("/save")
    public boolean save(@RequestBody Brand brand){
        try{
            brandService.save(brand);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /** 修改品牌 */
    @PostMapping("/update")
    public boolean update(@RequestBody Brand brand){
        try{
            brandService.update(brand);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /** 根据id删除品牌 */
    @GetMapping("/delete")
    public boolean delete(Long[] ids){
        try {
            brandService.deleteAll(ids);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 查询所有的品牌 */
    @GetMapping("/findBrandList")
    public List<Map<String,Object>> findBrandList(){
        return brandService.findAllByIdAndName();
    }

}