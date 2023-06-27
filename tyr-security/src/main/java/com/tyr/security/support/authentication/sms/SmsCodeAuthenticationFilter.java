package com.tyr.security.support.authentication.sms;


import com.tyr.security.support.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 功能描述 :手机短信认证登陆过滤器
 */
@Slf4j
public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    //发送短信验证码 或 验证短信验证码时，传递手机号的参数的名称[mobile]
    private String mobileParameter = SecurityConstants.DEFAULT_MOBILE_PARAMETER;

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/auth/sms",
            "POST");

    private boolean postOnly = true;

    public SmsCodeAuthenticationFilter() {
        // 拦截该路径，如果是访问该路径，则标识是需要短信登录
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        log.info("拦截该路径，如果是访问该路径，则标识是需要短信登录");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !request.getMethod().equals("POST")){
            throw new AuthenticationServiceException("不支持该认证方法: " + request.getMethod());
        }
        String mobile = obtainMobile(request);
        mobile = (mobile != null) ? mobile.trim() : "";
        SmsCodeAuthenticationToken authRequest = SmsCodeAuthenticationToken.unauthenticated(mobile);

        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /*
     * 功能描述：提供身份验证请求的详细属性
     * 入参：[request 为此创建身份验证请求, authRequest 详细信息集的身份验证请求对象]
     */
    protected void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    /*
     * 功能描述：设置用于获取用户名的参数名的登录请求。
     * 入参：[usernameParameter 默认为“用户名”。]
     */
    public void setMobileParameter(String mobileParameter) {
        Assert.hasText(mobileParameter, "手机号参数不能为空");
        this.mobileParameter = mobileParameter;
    }

    /*
     * 功能描述：获取手机号
     */
    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(this.mobileParameter);
    }

    /*
     * 功能描述：定义此筛选器是否只允许HTTP POST请求。如果设置为true，则接收不到POST请求将立即引发异常并不再继续身份认证
     */
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getMobileParameter() {
        return mobileParameter;
    }
}
