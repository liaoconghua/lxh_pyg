package cn.pyg.sellergoods.service.impl;

import cn.pyg.mapper.ItemCatMapper;
import cn.pyg.pojo.ItemCat;
import cn.pyg.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service(interfaceName = "cn.pyg.service.ItemCatService")
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatMapper itemCatMapper;

    @Override
    public void save(ItemCat itemCat) {

    }

    @Override
    public void update(ItemCat itemCat) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public ItemCat findOne(Serializable id) {
        return null;
    }

    @Override
    public List<ItemCat> findAll() {
        return null;
    }

    @Override
    public List<ItemCat> findByPage(ItemCat itemCat, int page, int rows) {
        return null;
    }

    /** 根据商品id分类查询 */
    @Override
    public List<ItemCat> findItemCatByParentId(Long parentId) {

        try {
            // 封装查询对象
            ItemCat itemCat = new ItemCat();
            itemCat.setParentId(parentId);
            return itemCatMapper.select(itemCat);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
