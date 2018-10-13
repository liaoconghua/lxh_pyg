package cn.pyg.manager.controller;

import cn.pyg.common.pojo.PageResult;
import cn.pyg.pojo.Goods;
import cn.pyg.service.GoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * 商品审核
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference(timeout = 100000000)
    private GoodsService goodsService;

    @GetMapping("/findByPage")
    public PageResult findByPage(Goods goods, Integer page, Integer rows) {
        try {
            /** 添加查询条件 */
            goods.setAuditStatus("0");
            /** GET请求中文转码 */
            if (StringUtils.isNoneBlank(goods.getGoodsName())) {
                goods.setGoodsName(new String(
                        goods.getGoodsName().getBytes("ISO8859-1"), "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goodsService.findByPage(goods, page, rows);
    }

    /**
     * 批量修改状态
     * @param ids
     * @param status
     * @return
     */
    @GetMapping("/updateStatus")
    public boolean updateStatus(Long[] ids, String status){
        try {
            goodsService.updateStatus(ids, status);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("/deleteById")
    public boolean deleteById(Long[] ids){
        try{
            goodsService.deleteById(ids);
            return true;
        }catch (Exception ex){
            return false;
        }
    }
}
