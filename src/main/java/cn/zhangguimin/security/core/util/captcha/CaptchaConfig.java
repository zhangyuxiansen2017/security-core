package cn.zhangguimin.security.core.util.captcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mr. Zhang
 * @description 验证码配置
 * @date 2019-03-28 11:47
 * @website https://www.zhangguimin.cn
 */
@Configuration
@EnableConfigurationProperties(CaptchaProperties.class)
class CaptchaConfig {
    @Autowired
    private CaptchaProperties captcha;

    @Bean
    @ConditionalOnMissingBean(name = "captchaServlet")
    public ServletRegistrationBean captchaServlet() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new CaptchaServlet(captcha), "/captchaImage");
        return registrationBean;
    }
}