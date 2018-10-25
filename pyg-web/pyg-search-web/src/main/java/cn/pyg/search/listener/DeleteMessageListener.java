package cn.pyg.search.listener;

import cn.pyg.service.GoodsService;
import cn.pyg.service.ItemSearchService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.jms.listener.SessionAwareMessageListener;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.Serializable;
import java.util.Arrays;

/**
 * 删除商品索引消息监听器
 */
public class DeleteMessageListener implements SessionAwareMessageListener<ObjectMessage>{

    @Reference(timeout = 10000000)
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(ObjectMessage objectMessage, Session session) throws JMSException {
        // 获取到参数
        Long[] ids = (Long[])objectMessage.getObject();
        System.out.println("================DeleteMessageListener================");
        System.out.println(Arrays.toString(ids));
        // 根据ids删除索引对应数据
        itemSearchService.delete(Arrays.asList(ids));
    }
}
