package cn.zhangguimin.security.config;

import cn.zhangguimin.security.config.properties.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Mr. Zhang
 * @description 登录成功后需要处理的事情，比如加经验，签到+1。。。。
 * @date 2019-03-27 22:17
 * @website https://www.zhangguimin.cn
 */
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("登录成功,处理其他事情。。。。。");
        UserDetails user = (UserDetails) authentication.getPrincipal();
        Boolean delete = redisTemplate.delete(user.getUsername());
        System.out.println(delete);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
