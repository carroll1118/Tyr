package com.tyr.security.support.handler;


import com.tyr.security.support.constant.SecurityConstants;
import com.tyr.security.support.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


public class TyrLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();

        // 生成JWT，并放置到请求头中
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = JwtUtils.createToken(userDetails.getUsername(), userDetails.getPassword());
        response.setHeader(SecurityConstants.Authorization, token);

        redisTemplate.opsForValue().set(userDetails.getUsername(),token,100000, TimeUnit.SECONDS);

        outputStream.write(("SuccessLogin").getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
