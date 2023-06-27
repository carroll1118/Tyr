package com.tyr.security.support.authentication.mail;


import com.tyr.security.support.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 功能描述 :邮箱认证登陆过滤器
 */
@Slf4j
public class MailAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    //发送邮箱验证码 或 验证验证码时，传递邮箱号的参数的名称[mail]
    private String mailParameter = SecurityConstants.DEFAULT_MAIL_PARAMETER;

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/auth/mail",
            "POST");

    private boolean postOnly = true;

    public MailAuthenticationFilter() {
        // 拦截该路径，如果是访问该路径，则标识是需要邮箱登录
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        log.info("拦截该路径，如果是访问该路径，则标识是需要邮箱登录");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")){
            throw new AuthenticationServiceException("不支持该认证方法: " + request.getMethod());
        }
        String mail = obtainMail(request);
        mail = (mail != null) ? mail.trim() : "";
        MailAuthenticationToken authRequest = MailAuthenticationToken.unauthenticated(mail);

        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /*
     * 功能描述：提供身份验证请求的详细属性
     * 入参：[request 为此创建身份验证请求, authRequest 详细信息集的身份验证请求对象]
     */
    protected void setDetails(HttpServletRequest request, MailAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    /*
     * 功能描述：设置用于获取用户名的参数名的登录请求。
     * 入参：[usernameParameter 默认为“用户名”。]
     */
    public void setMailParameter(String mailParameter) {
        Assert.hasText(mailParameter, "邮箱号参数不能为空");
        this.mailParameter = mailParameter;
    }

    /**
     * 获取邮箱号
     * @param request
     * @return
     */
    private String obtainMail(HttpServletRequest request) {
        return request.getParameter(this.mailParameter);
    }

    /**
     * 定义此筛选器是否只允许HTTP POST请求。如果设置为true，则接收不到POST请求将立即引发异常并不再继续身份认证
     */
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getMailParameter() {
        return mailParameter;
    }
}
