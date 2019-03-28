package cn.zhangguimin.security.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
/**
 * @description 主启动类
 * @author Mr. Zhang
 * @date 2019-03-28 11:45
 * @website https://www.zhangguimin.cn
 */
@EnableCaching
//@EnableRedisHttpSession
@SpringBootApplication

public class SecurityCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityCoreApplication.class, args);
    }

}
