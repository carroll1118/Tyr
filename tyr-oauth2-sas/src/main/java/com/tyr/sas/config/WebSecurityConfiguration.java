package com.tyr.sas.config;

import com.tyr.sas.support.core.TyrDaoAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @Author：carroll
 */
@EnableWebSecurity(debug = true)
public class WebSecurityConfiguration {

    // 用于身份验证的Spring Security过滤器链。
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated()
                )
                // Form login handles the redirect to the login page from the
                // authorization server filter chain
                .formLogin(Customizer.withDefaults());

        http.authenticationProvider(new TyrDaoAuthenticationProvider());
        return http.build();
    }
}
