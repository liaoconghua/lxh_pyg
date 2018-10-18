package cn.pyg.content.service.impl;

import cn.pyg.common.pojo.PageResult;
import cn.pyg.mapper.ContentMapper;
import cn.pyg.pojo.Content;
import cn.pyg.service.ContentService;
import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.tools.internal.xjc.generator.bean.field.ContentListField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import java.io.Serializable;
import java.util.Arrays;
/**
 * ContentServiceImpl 服务接口实现类
 * @date 2018-10-13 11:20:45
 * @version 1.0
 */
@Service(interfaceName = "cn.pyg.service.ContentService")
@Transactional
public class ContentServiceImpl implements ContentService {

	@Autowired
	private ContentMapper contentMapper;
	@Autowired
    private RedisTemplate redisTemplate;

	/** 添加方法 */
	public void save(Content content){
		try {
			contentMapper.insertSelective(content);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 修改方法 */
	public void update(Content content){
		try {
			contentMapper.updateByPrimaryKeySelective(content);

			// 每次修改后删除缓存中的数据 , 下一次会去查询新的数据然后会再放到redis缓存中
            redisTemplate.delete("content");
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 根据主键id删除 */
	public void delete(Serializable id){
		try {
			contentMapper.deleteByPrimaryKey(id);

            // 清除redis缓冲
            redisTemplate.delete("content");
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 批量删除 */
	public void deleteAll(Serializable[] ids){
		try {
			// 创建示范对象
			Example example = new Example(Content.class);
			// 创建条件对象
			Example.Criteria criteria = example.createCriteria();
			// 创建In条件
			criteria.andIn("id", Arrays.asList(ids));
			// 根据示范对象删除
			contentMapper.deleteByExample(example);

			// 清除redis缓冲
            redisTemplate.delete("content");
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 根据主键id查询 */
	public Content findOne(Serializable id){
		try {
			return contentMapper.selectByPrimaryKey(id);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 查询全部 */
	public List<Content> findAll(){
		try {
			return contentMapper.selectAll();
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 多条件分页查询 */
	public PageResult findByPage(Content content, int page, int rows){
		try {
			PageInfo<Content> pageInfo = PageHelper.startPage(page, rows)
				.doSelectPageInfo(new ISelect() {
					@Override
					public void doSelect() {
						contentMapper.selectAll();
					}
				});
			return new PageResult(pageInfo.getTotal(),pageInfo.getList());
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 根据广告分类表id查询广告表列表 */
	@Override
	public List<Content> findContentByCategoryId(Long categoryId) {
	    // 定义广告数据
        List<Content> contentList = null;
	    try{ // 这里try catch 要是报错还可以照常进行之后的代码
	        // 首先从redis中获取数据
            contentList = (List<Content>) redisTemplate.boundValueOps("content").get();
            if (contentList != null && contentList.size() > 0){
				System.out.println("redis....");
                 return contentList;
            }
        }catch (RuntimeException re){
	    	re.printStackTrace();
		}

		try {
			// 创建示范对象
			Example example = new Example(Content.class);
			// 创建查询条件对象
			Example.Criteria criteria = example.createCriteria();
			// 添加条件: category_id = categoryId
			criteria.andEqualTo("categoryId",categoryId);
			// 添加条件status = 1
			criteria.andEqualTo("status",1);
			// 排序(升序) order by sort_order asc
			example.orderBy("sortOrder").asc();

            contentList = contentMapper.selectByExample(example);
            try{
				System.out.println("数据库....");
                // 存入redis缓存中
                 redisTemplate.boundValueOps("content").set(contentList);
            }catch (Exception e){}
            return contentList;
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}