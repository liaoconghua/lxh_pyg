package cn.pyg.shop.sevice;

import cn.pyg.pojo.Seller;
import cn.pyg.service.SellerService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义认证类
 * 需要实现 UserDetailsService
 */

public class UserDetailsServiceImpl implements UserDetailsService {

    // 注入商家服务接口的代理对象
    private SellerService sellerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 创建List集合封装角色
        List<SimpleGrantedAuthority> grantedAuths = new ArrayList<>();
        // 添加角色
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));

        // 根据登录名查询商家
        Seller seller = sellerService.findOne(username);
        // 判断商家是否为空,并且已审核
        if (seller != null && "1".equals(seller.getStatus())){
            // 返回用户信息对象
            // 返回用户信息对象
            return new User(username, "root", grantedAuths);
        }
        return null;
    }

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }
}
