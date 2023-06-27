package com.tyr.resource.config;


import com.tyr.resource.handler.SimpleAccessDeniedHandler;
import com.tyr.resource.handler.SimpleAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 资源服务器
 * <a href="https://docs.spring.io/spring-security/reference/5.7/servlet/oauth2/resource-server/index.html"></a>
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启鉴权服务
public class WebSecurityConfiguration {

    // 用于身份验证的Spring Security过滤器链。
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {

        // 所有请求都进行拦截
        http.authorizeRequests().anyRequest().authenticated();
        // 关闭session
        http.sessionManagement().disable();
        // 配置资源服务器的无权限，无认证拦截器等 以及JWT验证
        http.oauth2ResourceServer()
                .accessDeniedHandler(new SimpleAccessDeniedHandler())
                .authenticationEntryPoint(new SimpleAuthenticationEntryPoint())
                .jwt();
       /* http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                        .jwkSetUri("https://idp.example.com/.well-known/jwks.json")
                )
        );*/
        return http.build();
    }
}
