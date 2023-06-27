package com.tyr.security.support.authentication.jwt;

import cn.hutool.core.util.StrUtil;
import com.tyr.security.support.constant.SecurityConstants;
import com.tyr.security.support.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = request.getHeader(SecurityConstants.Authorization);
        // 没有jwt相当于匿名访问，若有一些接口是需要权限的，则不能访问这些接口
        if (StrUtil.isBlankOrUndefined(token)) {
            chain.doFilter(request, response);
            return;
        }
        if (JwtUtils.tokenExpired(token)) {
            throw new RuntimeException("token 已过期");
        }


        String username = JwtUtils.getUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 获取用户的权限等信息
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            //从redis中取出存储的token
            String redisToken = (String) redisTemplate.opsForValue().get(userDetails.getUsername());
            log.info("JwtAuthenticationFilter.doFilterInternal,redisToken:{}",redisToken);
            //验证令牌是否有效
            if (token.equals(redisToken) && JwtUtils.validateToken(token, userDetails)) {
                // 构建UsernamePasswordAuthenticationToken,这里密码为null，是因为提供了正确的JWT,实现自动登录
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 使用 SecurityContextHolder.getContext().setAuthentication(authentication)，可能会导致多线程之间的竞争。
                // SecurityContextHolder.getContext().setAuthentication(authentication);
                // 推荐 下面这种方式
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);

    }

}
