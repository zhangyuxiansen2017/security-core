package cn.zhangguimin.security.core.util.captcha;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mr. Zhang
 * @description CaptchaProperties
 * @date 2019-03-29 00:02
 * @website https://www.zhangguimin.cn
 */
@Configuration
@EnableConfigurationProperties(Captcha.class)
public class CaptchaProperties {
}
