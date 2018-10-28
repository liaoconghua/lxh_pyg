package cn.pyg.search.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取登录用户名
 */
@RestController
@RequestMapping("/user")
public class LoginController {

    @GetMapping("/showName")
    public Map<String,String> showName(HttpServletRequest request){
        String username = request.getRemoteUser();
        Map<String,String> map = new HashMap<>();
        map.put("loginName", username);

        return map;

    }
}
