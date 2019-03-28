package cn.zhangguimin.security.core.util.captcha;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Mr. Zhang
 * @description 验证码参数
 * @date 2019/3/28 13:47
 * @website https://www.zhangguimin.cn
 */
@Data
@ConfigurationProperties(prefix = "zgm.captcha")
public class Captcha {

    private String code = "captchaCode";

    private String validity = "120";

    private String border = "yes";

    private String borderColor = "105,179,90";

    private String fontColor = "blue";

    private String imageWidth = "200";

    private String imageHeight = "40";

    private String fontSize = "38";

    private String charSpac = "5";

    private String charLength = "6";

    private String fontNames = "Arial,Courier";

    private String noiseColor = "white";
}
