package cn.zhangguimin.security.core.config;

import cn.zhangguimin.security.core.exception.ValidateCaptchaException;
import cn.zhangguimin.security.core.util.captcha.Captcha;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Mr. Zhang
 * @description 验证码过滤器
 * @date 2019/3/28 14:32
 * @website https://www.zhangguimin.cn
 */
public class ValidateCaptchaFilter extends OncePerRequestFilter {


    private AuthenticationFailureHandler authenticationFailureHandler;

    public final static String KAPTCHA_SESSION_KEY = "KAPTCHA_SESSION_KEY";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("/login".equals(request.getRequestURI()) && "post".equalsIgnoreCase(request.getMethod())) {

            try {
                validate(request);
            } catch (ValidateCaptchaException e) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void validate(HttpServletRequest request) {

        HttpSession session = request.getSession();

        Captcha captcha = (Captcha) session.getAttribute(KAPTCHA_SESSION_KEY);

        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request, "code");
        } catch (ServletRequestBindingException e) {
            throw new ValidateCaptchaException("获取验证码的值失败");
        }

        if (StringUtils.isEmpty(codeInRequest)) {
            throw new ValidateCaptchaException("验证码的值不能为空");
        }

        if (captcha == null) {
            throw new ValidateCaptchaException("验证码不存在");
        }

        if (captcha.isExpried()) {
            session.removeAttribute(KAPTCHA_SESSION_KEY);
            throw new ValidateCaptchaException("验证码已过期");
        }

        if (!codeInRequest.equals(captcha.getCode())) {
            throw new ValidateCaptchaException("验证码不匹配");
        }

        session.removeAttribute(KAPTCHA_SESSION_KEY);
    }

    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return authenticationFailureHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }
}
