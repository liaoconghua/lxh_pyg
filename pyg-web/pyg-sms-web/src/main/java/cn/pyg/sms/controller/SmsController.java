package cn.pyg.sms.controller;

import cn.pyg.service.SmsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信发送接口
 */
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Reference(timeout = 100000000)
    private SmsService smsService;

    @PostMapping("/sendSms")
    public Map<String,Object> sendSms(String phone, String signName,
                                      String templateCode, String templateParam){
        // 发送短信
        boolean success = smsService.sendSms(phone, signName, templateCode, templateParam);

        Map<String,Object> map = new HashMap<>();
        map.put("success", success);

        return map;
    }


}
