package cn.pyg.sellergoods.service.impl;

import cn.pyg.common.pojo.PageResult;
import cn.pyg.mapper.SpecificationMapper;
import cn.pyg.mapper.SpecificationOptionMapper;
import cn.pyg.pojo.Specification;
import cn.pyg.service.SpecificationService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service(interfaceName = "cn.pyg.service.SpecificationService")
@Transactional
public class SpecificationServiceImpl implements SpecificationService{
    /** 注入数据访问接口代理对象 */
    @Autowired
    private SpecificationMapper specificationMapper;
    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;

    @Override
    public void save(Specification specification) {
        try{
            specificationMapper.insertSelective(specification);
            specificationOptionMapper.save(specification);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
    @Override
    public void update(Specification specification) {
    }
    @Override
    public void delete(Serializable id) {
    }
    @Override
    public void deleteAll(Serializable[] ids) {
    }
    @Override
    public Specification findOne(Serializable id) {
        return null;
    }
    @Override
    public List<Specification> findAll() {
        return null;
    }
    /** 多条件分页查询规格 */
    @Override
    public PageResult findByPage(Specification specification,
                                 int page, int rows) {
        try {
            /** 开始分页 */
            PageInfo<Specification> pageInfo = PageHelper.startPage(page,rows)
                    .doSelectPageInfo(new ISelect() {
                        @Override
                        public void doSelect() {
                            specificationMapper.findAll(specification);
                        }
                    });
            return new PageResult(pageInfo.getTotal(),
                    pageInfo.getList());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /** 查询所有的规格 */
    @Override
    public List<Map<String, Object>> findAllByIdAndName() {
        return specificationMapper.findAllByIdAndName();
    }

}