package com.tyr.security.support.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * AuthenticationEntryPoint 用于发送一个要求客户端提供凭证的HTTP响应。
 *
 * 有时，客户端会主动包含凭证（如用户名和密码）来请求资源。在这些情况下，Spring Security 不需要提供要求客户端提供凭证的HTTP响应，因为这些凭证已经被包括在内。
 *
 * 在其他情况下，客户端向他们未被授权访问的资源发出未经认证的请求。在这种情况下， AuthenticationEntryPoint 的实现被用来请求客户端的凭证。 AuthenticationEntryPoint 的实现可能会执行 重定向到一个登录页面，用 WWW-Authenticate 头来响应，或采取其他行动。
 *
 */
public class TyrJwtAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {

        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();

        outputStream.write(("请先登录").getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}