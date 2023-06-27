package com.tyr.sas.support.core;

import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;

/**
 * @Author：carroll
 * 自定义OAuth2Token生成Service
 * 从提供的 OAuth2TokenContext 中包含的信息生成 OAuth2Token,生成的 OAuth2Token 主要取决于 OAuth2TokenContext 中指定的 OAuth2TokenType 类型。
 * 实现参考 OAuth2AccessTokenGenerator
 * <a href="https://docs.spring.io/spring-authorization-server/docs/0.4.2/reference/html/core-model-components.html#oauth2-token-generator">https://docs.spring.io/spring-authorization-server/docs/0.4.2/reference/html/core-model-components.html#oauth2-token-generator</a>
 */
public class TyrOAuth2TokenGenerator implements OAuth2TokenGenerator<OAuth2AccessToken> {

    private OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer;

    /**
     *
     * @param context the context containing the OAuth 2.0 Token attributes
     *  OAuth2TokenContext 是一个上下文对象，用于保存与 OAuth2Token 关联的信息，由 OAuth2TokenGenerator 和 OAuth2TokenCustomizer 使用。
     * @return
     */
    @Override
    public OAuth2AccessToken generate(OAuth2TokenContext context) {
        return null;
    }

    public void setAccessTokenCustomizer(OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer) {
        Assert.notNull(accessTokenCustomizer, "accessTokenCustomizer cannot be null");
        this.accessTokenCustomizer = accessTokenCustomizer;
    }
}
