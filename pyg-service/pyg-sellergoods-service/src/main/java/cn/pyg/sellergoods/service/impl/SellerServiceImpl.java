package cn.pyg.sellergoods.service.impl;

import cn.pyg.common.pojo.PageResult;
import cn.pyg.mapper.SellerMapper;
import cn.pyg.pojo.Seller;
import cn.pyg.service.SellerService;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商品入驻的服务层
 */
@Service(interfaceName = "cn.pyg.service.SellerService")
@Transactional
public class SellerServiceImpl implements SellerService {

    // 注入
    @Autowired
    private SellerMapper sellerMapper;

    @Override
    public void save(Seller seller) {
        try {
            // 首先初始化状态为 0
            seller.setStatus("0");
            // 初始化创建时间
            seller.setCreateTime(new Date());
            // 保存
            sellerMapper.insertSelective(seller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    /** 根据主键id查询商家 */
    @Override
    public Seller findOne(Serializable id) {
        return sellerMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }

    @Override
    public PageResult findByPage(Seller seller, int page, int rows) {
        try {
            // 开启分页
            PageInfo<Seller> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
                @Override
                public void doSelect() {
                    // 创建示范对象
                    Example example = new Example(Seller.class);
                    // 创建条件对象
                    Example.Criteria criteria = example.createCriteria();
                    // 审核状态码
                    if (seller != null && !StringUtils.isEmpty(seller.getStatus())) {
                        // status = ?
                        criteria.andEqualTo("status", seller.getStatus());
                    }
                    // 公司名称
                    if (seller != null && !StringUtils.isEmpty(seller.getName())) {
                        // name like ?
                        criteria.andLike("name", "%" + seller.getName() + "%");
                    }
                    // 店铺名称
                    if (seller != null && !StringUtils.isEmpty(seller.getNickName())) {
                        // nick_name like ?
                        criteria.andLike("nickName", "%" + seller.getNickName() + "%");
                    }
                    sellerMapper.selectByExample(example);
                }
            });
            return new PageResult(pageInfo.getTotal(), pageInfo.getList());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 修改商家状态
     */
    @Override
    public void updateStatus(String sellerId, String status) {

        Seller seller = new Seller();
        seller.setSellerId(sellerId);
        seller.setStatus(status);
        sellerMapper.updateByPrimaryKeySelective(seller);
    }

}
