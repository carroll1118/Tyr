package com.tyr.security.support.authentication.mail;

import cn.hutool.core.util.StrUtil;
import com.tyr.security.support.constant.SecurityConstants;
import com.tyr.security.support.core.MyUserDetailsService;
import com.tyr.security.support.exception.MailCodeException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 提供短信登录处理的实现类
 * 短信处理器MailAuthenticationProvider，用于匹配用户信息，如果认证成功加入到认证成功队列
 */

@Data
@Slf4j
public class MailAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("MailAuthenticationProvider");
        MailAuthenticationToken authenticationToken = (MailAuthenticationToken) authentication;
        String mail = determineMobile(authenticationToken);

        checkMailCode(mail);

        UserDetails userDetails = userDetailsService.loadUserByMobile(mail);

        MailAuthenticationToken result = MailAuthenticationToken.authenticated(userDetails , authentication.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }


    private void checkMailCode(String mail) throws AuthenticationException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //获取验证码
        String mailCode = request.getParameter("mailCode");
        log.info("mail:{}, mailCode:{}",mail, mailCode);
        String codeKey = SecurityConstants.MAIL_CODE_PREFIX + mail;
        String correctCode = (String) redisTemplate.opsForValue().get(codeKey);
        log.info("correctCode:{}",correctCode);
        // 验证码比对
        if (StrUtil.isBlank(correctCode) || !mail.equals(correctCode)) {
            throw new MailCodeException("验证码不正确");
        }
        // 比对成功删除缓存的验证码
        redisTemplate.delete(codeKey);

    }

    /**
     * 通过不同的Token进行不同的Provider验证
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return MailAuthenticationToken.class.isAssignableFrom(authentication);
    }


    private String determineMobile(Authentication authentication) {
        return (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : (String) authentication.getPrincipal();
    }
}
