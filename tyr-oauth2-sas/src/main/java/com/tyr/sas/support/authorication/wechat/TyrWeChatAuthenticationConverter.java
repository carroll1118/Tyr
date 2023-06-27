package com.tyr.sas.support.authorication.wechat;

import com.tyr.sas.support.authorication.TyrAbstractAuthenticationConverter;
import com.tyr.sas.support.constant.SecurityConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

public class TyrWeChatAuthenticationConverter extends TyrAbstractAuthenticationConverter<TyrWeChatAuthenticationToken> {

    @Override
    public TyrWeChatAuthenticationToken getAuthenticationToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters) {
        return new TyrWeChatAuthenticationToken(new AuthorizationGrantType(SecurityConstants.WX_CHAT),clientPrincipal,requestedScopes,additionalParameters);
    }

    @Override
    public boolean support(String grantType) {
        return SecurityConstants.WX_CHAT.equals(grantType);
    }
}
