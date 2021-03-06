package cn.zhangguimin.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @description 主启动类
 * @author Mr. Zhang
 * @date 2019-03-28 11:45
 * @website https://www.zhangguimin.cn
 */
@EnableCaching
@EnableRedisHttpSession
@SpringBootApplication
@EnableAuthorizationServer
@EnableResourceServer
public class SecurityCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityCoreApplication.class, args);
    }

}
