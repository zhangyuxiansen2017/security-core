package cn.zhangguimin.security.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

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
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private DataSource dataSource;


    /**
     * 验证码--表单登录--登录页--提交登录url--登录失败url--登录成功Handler--登录失败Handler
     * --记住我--记住我token持久化--记住我时间--登录实现--
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
            http
                .addFilterBefore(validateCaptchaFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .failureUrl("/login?error")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailHandler)

                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(24*60*60)
                .userDetailsService(userDetailsService)
                .and()

                .authorizeRequests()
                .antMatchers("/login", "/captchaImage").permitAll()
                .anyRequest()
                .authenticated()
                .and()

                .logout()
                .logoutUrl("/logout")
                .and()

                .csrf()
                .disable();
    }

    /**
     * 使用PasswordEncoder加密解密密码，
     * 优点：相同密码每次都会产生不同值
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * remember-me配置的token存储数据源
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        //首次启动需要创建数据库表
        //tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }
}
