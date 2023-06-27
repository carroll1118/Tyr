package com.tyr.sas.support.authorication.mail;

import com.tyr.sas.support.authorication.TyrAbstractAuthenticationConverter;
import com.tyr.sas.support.authorication.sms.TrySmsCodeAuthenticationToken;
import com.tyr.sas.support.constant.SecurityConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.Set;

public class TryMailAuthenticationConverter extends TyrAbstractAuthenticationConverter<TryMailAuthenticationToken> {

    @Override
    public TryMailAuthenticationToken getAuthenticationToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters) {
        return new TryMailAuthenticationToken(new AuthorizationGrantType(SecurityConstants.MAIL),clientPrincipal,requestedScopes,additionalParameters);
    }

    @Override
    public boolean support(String grantType) {
        return SecurityConstants.MAIL.equals(grantType);
    }

    @Override
    public void validParameters(MultiValueMap<String, String> parameters) {


    }
}
