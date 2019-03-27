package cn.zhangguimin.security.core.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Mr. Zhang
 * @description 登录
 * @date 2019-03-27 20:30
 * @website https://www.zhangguimin.cn
 */
@Controller
public class LoginController {

    @GetMapping(value = "/login")
    public String login(){
        return "login";
    }

    @GetMapping(value = "/")
    public String index(){
        return "index";
    }

    /**
     * 可以直接  getCurrentUser(Authentication  authentication) 直接return authentication，这样会返回很多没必要的数据
     * 			可以使用 @AuthenticationPrincipal UserDetails user。返回的只是UserDetails信息
     * @param user
     * @return
     */
    @GetMapping("/me")
    @ResponseBody
    public Object getCurrentUser(@AuthenticationPrincipal UserDetails user) {
        return user;
    }
}
