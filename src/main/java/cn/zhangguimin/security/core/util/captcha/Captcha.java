package cn.zhangguimin.security.core.util.captcha;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Mr. Zhang
 * @description 图片存储
 * @date 2019/3/28 13:47
 * @website https://www.zhangguimin.cn
 */
@Data
public class Captcha implements Serializable {

    private String code;

    private LocalDateTime expireTime;

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }

    public Captcha( String code, int expireTime) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireTime);
    }
}
