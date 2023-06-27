package com.tyr.sas.support.core;

import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * @Author：carroll
 * 自定义 OAuth2Token 属性的功能，OAuth2TokenGenerator使用它来让它在生成之前自定义 OAuth2Token 的属性。
 */
public class TyrOAuth2TokenCustomizer  implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {
    @Override
    public void customize(OAuth2TokenClaimsContext context) {

    }
}
