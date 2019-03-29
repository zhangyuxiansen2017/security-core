package cn.zhangguimin.security.config.captcha;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;

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
 * @description CaptchaServlet
 * @date 2019/3/29 9:04
 * @website https://www.zhangguimin.cn
 */
public class CaptchaServlet extends HttpServlet implements Servlet {

    private Producer captchaProducer = null;

    private String sessionKeyValue = null;

    private String sessionKeyDateValue = null;

    private CaptchaProperties captcha;

    public CaptchaServlet(CaptchaProperties captcha) {
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
        properties.setProperty("kaptcha.textproducer.char.spac", captcha.getType().equals("char") ? "35":"5");
        properties.setProperty("kaptcha.textproducer.char.length", captcha.getCharLength());
        properties.setProperty("kaptcha.textproducer.font.names", captcha.getFontNames());
        properties.setProperty("kaptcha.textproducer.impl", captcha.getTextproducerImpl());

        properties.setProperty("kaptcha.image.width", captcha.getImageWidth());
        properties.setProperty("kaptcha.image.height", captcha.getImageHeight());

        properties.setProperty("kaptcha.session.key", captcha.getCode());
        properties.setProperty("kaptcha.noise.color", captcha.getNoiseColor());
        properties.setProperty("kaptcha.noise.impl", captcha.getNoiseImpl());

        properties.setProperty("kaptcha.obscurificator.impl", captcha.getObscurificatorImpl());

        ImageIO.setUseCache(false);

        Config config = new Config(properties);
        this.captchaProducer = config.getProducerImpl();
        this.sessionKeyValue = config.getSessionKey();
        this.sessionKeyDateValue = config.getSessionDate();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setDateHeader("Expires", 0);
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
        resp.setHeader("Pragma", "no-cache");
        resp.setContentType("image/jpeg");

        String capStr = null;
        String code = null;
        BufferedImage bi = null;
        if ("math".equals(captcha.getType())) {
            String capText = captchaProducer.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            bi = captchaProducer.createImage(capStr);
        } else {
            capStr = code = captchaProducer.createText();
            bi = captchaProducer.createImage(capStr);
        }

        req.getSession().setAttribute(this.sessionKeyValue, code);
        req.getSession().setAttribute(this.sessionKeyDateValue, LocalDateTime.now().plusSeconds(Integer.valueOf(captcha.getValidity())));

        ServletOutputStream out = resp.getOutputStream();
        ImageIO.write(bi, "jpeg", out);
    }
}
