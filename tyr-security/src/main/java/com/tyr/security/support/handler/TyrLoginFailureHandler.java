package com.tyr.security.support.handler;

import com.tyr.security.support.exception.MailCodeException;
import com.tyr.security.support.exception.SmsCodeException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TyrLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();

        String errorMessage = "用户名或密码错误";
        if (exception instanceof SmsCodeException) {
            errorMessage = "手机验证码错误";
        } else if (exception instanceof MailCodeException) {
            errorMessage = "邮箱验证码错误";
        }
        outputStream.write(errorMessage.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
