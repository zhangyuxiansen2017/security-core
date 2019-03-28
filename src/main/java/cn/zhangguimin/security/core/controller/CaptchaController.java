package cn.zhangguimin.security.core.controller;

import cn.zhangguimin.security.core.util.captcha.Captcha;
import com.google.code.kaptcha.Producer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Mr. Zhang
 * @description 图片验证码
 * @date 2019/3/28 11:49
 * @website https://www.zhangguimin.cn
 */
@Controller
public class CaptchaController {

    @Resource
    private Producer captchaProducer;

    @Resource
    private Producer captchaProducerMath;

    public final static String KAPTCHA_SESSION_KEY = "KAPTCHA_SESSION_KEY";

    /**
     * 验证码生成
     */
    @GetMapping(value = "/captchaImage")
    public void getKaptchaImage(HttpServletRequest request, HttpServletResponse response) {
        ServletOutputStream out = null;
        HttpSession session = request.getSession();
        String type = request.getParameter("type");
        String capStr = null;
        String code = null;
        BufferedImage bi = null;
        if ("math".equals(type)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            bi = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(type)) {
            capStr = code = captchaProducer.createText();
            bi = captchaProducer.createImage(capStr);
        }
        Captcha captcha = new Captcha(code, 120);
        session.setAttribute(KAPTCHA_SESSION_KEY, captcha);

        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        try {
            out = response.getOutputStream();
            ImageIO.write(bi, "jpg", out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
