package cn.zhangguimin.security.core.util.captcha;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.imageio.ImageIO;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;

/**
 * @author Mr. Zhang
 * @description 验证码配置
 * @date 2019-03-28 11:47
 * @website https://www.zhangguimin.cn
 */
@Configuration
class CaptchaConfig {
    @Autowired
    private Captcha captcha;
    @Bean
    public ServletRegistrationBean myServlet() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new MyKaptchaServlet(captcha), "/captchaImage");
        return registrationBean;
    }
}
class MyKaptchaServlet extends HttpServlet implements Servlet {
    private Producer kaptchaProducer = null;

    private String sessionKeyValue = null;

    private String sessionKeyDateValue = null;

    private Captcha captcha;

    public MyKaptchaServlet(Captcha captcha) {
        this.captcha = captcha;
    }

    @Override
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);

        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", captcha.getBorder());
        properties.setProperty("kaptcha.border.color", captcha.getBorderColor());
        properties.setProperty("kaptcha.textproducer.font.size", captcha.getFontSize());
        properties.setProperty("kaptcha.textproducer.font.color", captcha.getFontColor());
        properties.setProperty("kaptcha.textproducer.impl", "cn.zhangguimin.security.core.util.captcha.KaptchaTextCreator");
        properties.setProperty("kaptcha.textproducer.char.spac", captcha.getCharSpac());
        properties.setProperty("kaptcha.textproducer.char.length", captcha.getCharLength());
        properties.setProperty("kaptcha.textproducer.font.names", captcha.getFontNames());
        properties.setProperty("kaptcha.image.width", captcha.getImageWidth());
        properties.setProperty("kaptcha.image.height", captcha.getImageHeight());
        properties.setProperty("kaptcha.noise.color", captcha.getNoiseColor());
        properties.setProperty("kaptcha.session.key", captcha.getCode());
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");

        ImageIO.setUseCache(false);

        Config config = new Config(properties);
        this.kaptchaProducer = config.getProducerImpl();
        this.sessionKeyValue = config.getSessionKey();
        this.sessionKeyDateValue = config.getSessionDate();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setDateHeader("Expires", 0);
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
        resp.setHeader("Pragma", "no-cache");
        resp.setContentType("image/jpeg");

        String capText = this.kaptchaProducer.createText();
        String capStr = capText.substring(0, capText.lastIndexOf("@"));
        String code = capText.substring(capText.lastIndexOf("@") + 1);

        req.getSession().setAttribute(this.sessionKeyValue, code);
        req.getSession().setAttribute(this.sessionKeyDateValue, LocalDateTime.now().plusSeconds(Integer.valueOf(captcha.getValidity())));
        BufferedImage bi = this.kaptchaProducer.createImage(capStr);
        ServletOutputStream out = resp.getOutputStream();
        ImageIO.write(bi, "jpeg", out);
    }
}
