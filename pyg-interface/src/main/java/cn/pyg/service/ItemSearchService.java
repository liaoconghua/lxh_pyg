package cn.pyg.service;

import java.util.Map; /**
 * 搜索系统的接口
 */
public interface ItemSearchService {

    Map<String,Object> search(Map<String, Object> params);
}
