package com.tyr.security.config;

import com.tyr.security.support.authentication.jwt.JwtAuthenticationFilter;
import com.tyr.security.support.authentication.mail.MailAuthenticationFilter;
import com.tyr.security.support.authentication.mail.MailAuthenticationProvider;
import com.tyr.security.support.authentication.sms.SmsCodeAuthenticationFilter;
import com.tyr.security.support.authentication.sms.SmsCodeAuthenticationProvider;
import com.tyr.security.support.core.MyUserDetailsService;
import com.tyr.security.support.core.TyrSessionInformationExpiredStrategy;
import com.tyr.security.support.handler.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

/**
 * Spring Security 安全配置
 *  @EnableWebSecurity 是 Spring Security 用于启用Web安全的注解。
 * 典型的用法是该注解用在某个Web安全配置类上(实现了接口WebSecurityConfigurer或者继承自WebSecurityConfigurerAdapter)。
 */
@EnableWebSecurity
public class WebSecurityConfig {


    // 用于身份验证的Spring Security过滤器链。
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .authorizeRequests()
                    // 放行的路径
                    .antMatchers("/", "/home","/code/sms").permitAll()
                    // 除了放行的，其它都需要认证
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/hello")
                    .successHandler(new TyrLoginSuccessHandler())
                    .failureHandler(new TyrLoginFailureHandler())
                    .and()
                .rememberMe()
                    // Token存储  生产环境建议存储到Redis 或者 Mysql
                    .tokenRepository(new InMemoryTokenRepositoryImpl())
                    // 新增过期配置，单位秒，默认配置写的60秒
                    .tokenValiditySeconds(60)
                    .and()
                .logout()
                    .logoutSuccessHandler(new TyrLogoutSuccessHandler())
                    .and()
                // session 管理
                .sessionManagement()
                    // 基于token，就不需要session，可以设置不创建session
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                    //单用户登录，如果有一个登录了，同一个用户在其他地方登录将前一个剔除下线
                    //.maximumSessions(1).expiredSessionStrategy(new TyrSessionInformationExpiredStrategy())
                    //单用户登录，如果有一个登录了，同一个用户在其他地方不能登录
                    // .maximumSessions(1).maxSessionsPreventsLogin(true).and()
                    .and()
                // 异常处理
                .exceptionHandling()
                    .accessDeniedHandler(new TyrAccessDeniedHandler())
                    //其他异常处理类
                    .authenticationEntryPoint(new TyrJwtAuthenticationEntryPoint())
                    .and()
                // 自定义过滤器和权限认证Provider
                    .addFilterAfter(new JwtAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class)
                    .addFilterAfter(new SmsCodeAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class)
                    .addFilterAfter(new MailAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class)
                    .authenticationProvider(new SmsCodeAuthenticationProvider())
                    .authenticationProvider(new MailAuthenticationProvider())
                    .authenticationProvider(setDaoAuthenticationProvider())
                .csrf().disable();

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     *  用户密码登录模式
     * @return
     */
    private DaoAuthenticationProvider setDaoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(new MyUserDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        // 是否隐藏用户不存在异常，默认:true-隐藏；false-抛出异常；
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

}