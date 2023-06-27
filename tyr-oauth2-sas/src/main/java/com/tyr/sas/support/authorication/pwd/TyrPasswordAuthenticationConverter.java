package com.tyr.sas.support.authorication.pwd;

import com.tyr.sas.support.authorication.TyrAbstractAuthenticationConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

public class TyrPasswordAuthenticationConverter extends TyrAbstractAuthenticationConverter<TyrPasswordAuthenticationToken> {


    @Override
    public TyrPasswordAuthenticationToken getAuthenticationToken(Authentication clientPrincipal, Set requestedScopes, Map additionalParameters) {
        return new TyrPasswordAuthenticationToken(AuthorizationGrantType.PASSWORD,clientPrincipal,requestedScopes,additionalParameters);
    }

    @Override
    public boolean support(String grantType) {
        return AuthorizationGrantType.PASSWORD.getValue().equals(grantType);
    }
}
