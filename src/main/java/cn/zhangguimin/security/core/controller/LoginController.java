package cn.zhangguimin.security.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}
