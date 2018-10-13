package cn.pyg.sellergoods.service.impl;

import java.util.Date;

import cn.pyg.common.pojo.PageResult;
import cn.pyg.mapper.*;
import cn.pyg.pojo.*;
import cn.pyg.service.GoodsService;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 新增商品服务层
 */
@Service(interfaceName = "cn.pyg.service.GoodsService")
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsDescMapper goodsDescMapper;
    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SellerMapper sellerMapper;
    @Autowired
    private ItemMapper itemMapper;


    /**
     * 添加商品
     */
    @Override
    public void save(Goods goods) {
        try {
            // 设置未被审核状态
            goods.setAuditStatus("0");
            // 为商品描述对象设置ID
            goodsMapper.insertSelective(goods);

            // 往tb_goods_desc表插入数据
            goods.getGoodsDesc().setGoodsId(goods.getId());
            goodsDescMapper.insertSelective(goods.getGoodsDesc());

            // 首先判断是否是启动规格
            if ("1".equals(goods.getIsEnableSpec())) {

                // 迭代出商品信息 items
                for (Item item : goods.getItems()) {
                    StringBuilder title = new StringBuilder();
                    title.append(goods.getGoodsName());
                    // 把规格选项JSON字符串转化成Map集合
                    Map<String, Object> spec = JSON.parseObject(item.getSpec());
                    for (Object value : spec.values()) {
                        title.append("" + value);
                    }
                    // 定义SKU商品的标题
                    item.setTitle(title.toString());
                    // 设置SKU商品的其它信息
                    setItemInfo(item, goods);
                    // 保存
                    itemMapper.insertSelective(item);

                }
            }else {
                // SPU就是SKU
                Item item = new Item();
                // {spec:{}, price:0, num:9999, status:'0', isDefault:'0'}
                /** 设置SKU商品的价格 */
                item.setPrice(goods.getPrice());
                /** 设置SKU商品库存数据 */
                item.setNum(9999);
                /** 设置SKU商品启用状态 */
                item.setStatus("1");
                /** 设置是否默认*/
                item.setIsDefault("1");
                /** 设置规格选项 */
                item.setSpec("{}");

                /** 设置SKU商品的标题 */
                item.setTitle(goods.getGoodsName());

                // 设置SKU商品的其它信息
                setItemInfo(item, goods);
                itemMapper.insertSelective(item);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 设置SKU商品的其它信息 */
    private void setItemInfo(Item item, Goods goods) {
        // [{"color":"玫瑰金","url":"http://image.pinyougou.com/jd/wKgMg1qtOcWAbBlcAADGjJFNgRY299.jpg"},
        // {"color":"金色","url":"http://image.pinyougou.com/jd/wKgMg1qtOdqAI_RvAAC8m-WGcfc751.jpg"},
        // {"color":"银色","url":"http://image.pinyougou.com/jd/wKgMg1qtOfCAHUP7AACj8bfZXwA701.jpg"}]
        // SKU商品的图片
        List<Map> mapList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
        if (mapList != null && mapList.size() > 0) {
            item.setImage(mapList.get(0).get("url").toString());
        }
        // SKU商品的三级分类id
        item.setCategoryid(goods.getCategory3Id());
        // SKU商品的创建时间
        item.setCreateTime(new Date());
        // SKU商品的修改时间
        item.setUpdateTime(item.getCreateTime());
        // SKU商品关联的SPU的id
        item.setGoodsId(goods.getId());
        // SKU商品的商家的id
        item.setSellerId(goods.getSellerId());

        // SKU商品的三级分类名称
        ItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id());
        item.setCategory(itemCat != null ? itemCat.getName() : "");
        // SKU商品的品牌名称
        Brand brand = brandMapper.selectByPrimaryKey(goods.getBrandId());
        item.setBrand(brand != null ? brand.getName() : "");
        // SKU商品的店铺名称
        Seller seller = sellerMapper.selectByPrimaryKey(goods.getSellerId());
        item.setSeller(seller != null ? seller.getNickName() : "");
    }

    @Override
    public void update(Goods goods) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public Goods findOne(Serializable id) {
        return null;
    }

    @Override
    public List<Goods> findAll() {
        return null;
    }

    /** 分页查询商品审核列表 */
    @Override
    public PageResult findByPage(Goods goods, int page, int rows) {
        try {
            // 开启分页
            PageInfo<Map<String, Object>> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
                @Override
                public void doSelect() {
                    goodsMapper.findAll(goods);
                }
            });

            // 迭代商品的名称
            // 在页面已知的数据只有一二三级中id , 业务需求是要显示对应的名称, 所以需要拿到对应的id进行查询
            for (Map<String, Object> map : pageInfo.getList()) {
                ItemCat itemCat = itemCatMapper.selectByPrimaryKey(map.get("category1Id"));
                map.put("category1Name", itemCat != null ? itemCat.getName() : "");

                ItemCat itemCat2 = itemCatMapper.selectByPrimaryKey(map.get("category2Id"));
                map.put("category2Name", itemCat2 != null ? itemCat2.getName() : "");

                ItemCat itemCat3 = itemCatMapper.selectByPrimaryKey(map.get("category3Id"));
                map.put("category3Name", itemCat3 != null ? itemCat3.getName() : "");
            }

            return new PageResult(pageInfo.getTotal(), pageInfo.getList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 批量修改状态 */
    @Override
    public void updateStatus(Long[] ids, String status) {
        try {
            goodsMapper.updateStatus(ids,status,"audit_status");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 批量删除商品 */
    @Override
    public void deleteById(Long[] ids) {
        try {
            goodsMapper.updateStatus(ids,"1","is_delete");
        }catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
