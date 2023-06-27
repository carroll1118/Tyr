package com.tyr.sso.server.config;

import com.tyr.core.support.helper.TyrSsoLoginStore;
import com.tyr.core.support.util.JedisUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TyrSsoConfig implements InitializingBean, DisposableBean {

    @Value("${tyr.sso.redis.host}")
    private String redisHost;

    @Value("${tyr.sso.redis.port}")
    private int redisPort;

    @Value("${tyr.sso.redis.expire.minute}")
    private int redisExpireMinute;

    @Override
    public void afterPropertiesSet() {
        TyrSsoLoginStore.setRedisExpireMinute(redisExpireMinute);
        JedisUtil.init(redisHost,redisPort);
    }

    @Override
    public void destroy() throws Exception {
        JedisUtil.close();
    }

}
