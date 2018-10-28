package cn.pyg.cart.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录控制器
 */
@RestController
@RequestMapping("/user")
public class LoginController {

    /** 获取用户名 */
    @GetMapping("/showName")
    public Map<String,String> showName(HttpServletRequest request){

        // 获取登录用户名方法
        String name = request.getRemoteUser();
        Map<String,String> map = new HashMap<>();
        map.put("loginName", name);

        return map;

    }


}
