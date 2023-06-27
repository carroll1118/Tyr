package com.tyr.security.support.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * UserDetailsService接口用于加载用户特定的数据，它在整个框架中作为用户DAO使用，是验证提供者使用的策略。
 * 该接口只需要一个只读方法，这简化了对新的数据访问策略的支持。实现一个自定义的UserDetailsService
 */
@Slf4j
@Component
public class MyUserDetailsService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;

    public MyUserDetailsService() {
        setPasswordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * 从数据库/缓存中获取用户数据
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("登录用户名:{}",username);
        String password = passwordEncoder.encode("123456");
        return new SysUser(1,username,
                password,
                "admin",
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }

    public UserDetails loadUserByMobile(String mobile) {
        log.info("登录手机号:{}",mobile);
        // 获取用户信息
        String username = "admin";
        String password = passwordEncoder.encode("123456");
        return new SysUser(1,username,
                password,
                "admin",
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
