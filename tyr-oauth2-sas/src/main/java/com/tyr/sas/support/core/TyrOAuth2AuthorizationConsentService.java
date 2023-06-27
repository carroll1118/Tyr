package com.tyr.sas.support.core;

import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;

/**
 * @Author：carroll
 * 自定义确认授权Service 存储新授权同意和查询现有授权同意的中心组件  参考 JdbcOAuth2AuthorizationConsentService
 */
public class TyrOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    /**
     * @param authorizationConsent the {@link OAuth2AuthorizationConsent}
     * 表示来自 OAuth2 授权请求流的授权“同意”（决策），例如， authorization_code 授权，其中包含资源所有者授予客户端的权限。
     */
    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {

    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {

    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        return null;
    }
}
