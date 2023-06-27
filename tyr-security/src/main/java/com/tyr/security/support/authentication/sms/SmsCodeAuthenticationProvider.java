package com.tyr.security.support.authentication.sms;

import cn.hutool.core.util.StrUtil;
import com.tyr.security.support.constant.SecurityConstants;
import com.tyr.security.support.core.MyUserDetailsService;
import com.tyr.security.support.exception.SmsCodeException;
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
 * 短信处理器SmsCodeAuthenticationProvider，用于匹配用户信息，如果认证成功加入到认证成功队列
 */

@Data
@Slf4j
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("SmsCodeAuthenticationProvider");
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        String mobile = determineMobile(authenticationToken);

        checkSmsCode(mobile);

        UserDetails userDetails = userDetailsService.loadUserByMobile(mobile);

        SmsCodeAuthenticationToken result = SmsCodeAuthenticationToken.authenticated(userDetails , userDetails.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }


    private void checkSmsCode(String mobile) throws AuthenticationException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //获取验证码
        String smsCode = request.getParameter("smsCode");
        log.info("mobile:{}, smsCode:{}",mobile, smsCode);
        String codeKey = SecurityConstants.SMS_CODE_PREFIX + mobile;
        String correctCode = (String) redisTemplate.opsForValue().get(codeKey);
        log.info("correctCode:{}",correctCode);
        // 验证码比对
        if (StrUtil.isBlank(correctCode) || !smsCode.equals(correctCode)) {
            throw new SmsCodeException("验证码不正确");
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
        return (SmsCodeAuthenticationToken.class.isAssignableFrom(authentication));
    }


    private String determineMobile(Authentication authentication) {
        return (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : (String) authentication.getPrincipal();
    }
}
