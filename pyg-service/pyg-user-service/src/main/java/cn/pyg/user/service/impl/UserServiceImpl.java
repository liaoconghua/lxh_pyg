package cn.pyg.user.service.impl;

import cn.pyg.common.util.HttpClientUtils;
import cn.pyg.mapper.UserMapper;
import cn.pyg.pojo.User;
import cn.pyg.service.UserService;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service(interfaceName =  "cn.pyg.service.UserService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Value("${sms.url}")
    private String smsUrl;
    @Value("${sms.signName}")
    private String signName;
    @Value("${sms.templateCode}")
    private String templateCode;

    /** 发送短信验证码 */
    public boolean sendCode(String phone){
        try{
            /** 生成6位随机数 */
            String code = UUID.randomUUID().toString()
                    .replaceAll("-", "")
                    .replaceAll("[a-z|A-Z]","")
                    .substring(0, 6);
            System.out.println("验证码：" + code);
            /** 调用短信发送接口 */
            HttpClientUtils httpClientUtils = new HttpClientUtils(false);
            // 创建Map集合封装请求参数
            Map<String, String> param = new HashMap<>();
            param.put("phone", phone);
            param.put("signName", signName);
            param.put("templateCode", templateCode);
            param.put("templateParam", "{\"number\":\"" + code + "\"}");
            // 发送Post请求
            String content = httpClientUtils.sendPost(smsUrl, param);
            // 把json字符串转化成Map
            Map<String, Object> resMap = JSON.parseObject(content, Map.class);
            /** 存入Redis中(90秒) */
            redisTemplate.boundValueOps(phone).set(code, 90, TimeUnit.SECONDS);
            return (boolean)resMap.get("success");
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 检查短信验证码是否正确 */
    @Override
    public boolean checkSmsCode(String phone, String smsCode) {
        // 获取redis中存储的验证码
        String sysCode = redisTemplate.boundValueOps(phone).get();
        return StringUtils.isNoneBlank(sysCode) && sysCode.equals(smsCode);
    }

    @Override
    public void save(User user) {

        user.setCreated(new Date());
        user.setUpdated(user.getCreated());

        user.setPassword(DigestUtils.md5Hex(user.getPassword()));

        userMapper.insertSelective(user);

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public User findOne(Serializable id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public List<User> findByPage(User user, int page, int rows) {
        return null;
    }


}
