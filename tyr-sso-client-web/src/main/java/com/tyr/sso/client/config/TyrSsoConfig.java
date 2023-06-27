package com.tyr.sso.client.config;


import com.tyr.core.conf.TyrSsoConf;

import com.tyr.core.filter.web.TyrSsoWebFilter;
import com.tyr.core.support.util.JedisUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TyrSsoConfig implements DisposableBean {


    @Value("${tyr.sso.server}")
    private String tyrSsoServer;

    @Value("${tyr.sso.logout.path}")
    private String tyrSsoLogoutPath;

    @Value("${tyr.sso.redis.host}")
    private String redisHost;

    @Value("${tyr.sso.redis.port}")
    private int redisPort;

    @Value("${tyr-sso.excluded.paths}")
    private String tyrSsoExcludedPaths;


    @Bean
    public FilterRegistrationBean tyrSsoFilterRegistration() {

        // tyr-sso, redis init
         JedisUtil.init(redisHost,redisPort);

        // tyr-sso, filter init
        FilterRegistrationBean registration = new FilterRegistrationBean();

        registration.setName("TyrSsoWebFilter");
        registration.setOrder(1);
        registration.addUrlPatterns("/*");
        registration.setFilter(new TyrSsoWebFilter());
        registration.addInitParameter(TyrSsoConf.SSO_SERVER, tyrSsoServer);
        registration.addInitParameter(TyrSsoConf.SSO_LOGOUT_PATH, tyrSsoLogoutPath);
        registration.addInitParameter(TyrSsoConf.SSO_EXCLUDED_PATHS, tyrSsoExcludedPaths);

        return registration;
    }

    @Override
    public void destroy() throws Exception {

        // tyr-sso, redis close
        JedisUtil.close();
    }

}
