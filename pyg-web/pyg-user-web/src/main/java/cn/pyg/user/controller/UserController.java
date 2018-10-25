package cn.pyg.user.controller;

import cn.pyg.pojo.User;
import cn.pyg.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference(timeout = 1000000000)
    private UserService userService;

    @PostMapping("/save")
    public boolean save(@RequestBody User user, String smsCode){
        try{

           boolean ok =  userService.checkSmsCode(user.getPhone(), smsCode);
           if (ok){
               userService.save(user);
               return true;
           }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/sendCode")
    public boolean sendCode(String phone){
        try{
            // 发送验证码
            userService.sendCode(phone);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
}
