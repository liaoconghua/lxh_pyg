package cn.pyg.item.listener;

import cn.pyg.pojo.Goods;
import cn.pyg.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.listener.SessionAwareMessageListener;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.File;
import java.io.Serializable;

/**
 * 根据下架删除静态页面
 */
public class DeleteMessageListener implements SessionAwareMessageListener<ObjectMessage>{

    @Value("${pageDir}")
    private String pageDir;


    @Override
    public void onMessage(ObjectMessage objectMessage, Session session) throws JMSException {
        // 获取数据
        Long[] ids = (Long[])objectMessage.getObject();
        System.out.println("===============DeleteMessageListener===============");
        try {
            for (Long id : ids) {
                File file = new File(pageDir + id + ".html");
                // 判断是否存在
                if (file.exists()){
                    file.delete();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
