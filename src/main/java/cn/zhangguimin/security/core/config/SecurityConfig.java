package cn.zhangguimin.security.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Mr. Zhang
 * @description security配置
 * @date 2019-03-27 20:20
 * @website https://www.zhangguimin.cn
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private LoginFailHandler loginFailHandler;


    /**
     * loginPage:需要跳转的登录页，前提是antMatchers里将此请求放开访问权限
     * loginProcessingUrl：表单提交后请求链接，默认/login
     * successHandler：登录成功自定义Handler
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ValidateCaptchaFilter validateCaptchaFilter = new ValidateCaptchaFilter();
        validateCaptchaFilter.setAuthenticationFailureHandler(loginFailHandler);
        http.addFilterBefore(validateCaptchaFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .failureUrl("/login?error")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/login","/captchaImage").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf()
                .disable();
    }

    /**
     * 使用PasswordEncoder加密解密密码，
     *         优点：相同密码每次都会产生不同值
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
