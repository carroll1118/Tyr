package com.tyr.sas.support.authorication.baidu;

import com.tyr.sas.support.authorication.TyrAbstractAuthenticationConverter;
import com.tyr.sas.support.constant.SecurityConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.Set;

public class TyrBaiDuAuthenticationConverter extends TyrAbstractAuthenticationConverter<TyrBaiDuAuthenticationToken> {

    @Override
    public TyrBaiDuAuthenticationToken getAuthenticationToken(Authentication clientPrincipal, Set requestedScopes, Map additionalParameters) {
        return new TyrBaiDuAuthenticationToken(new AuthorizationGrantType(SecurityConstants.BAIDU),clientPrincipal,requestedScopes,additionalParameters);
    }

    @Override
    public boolean support(String grantType) {
        return SecurityConstants.BAIDU.equals(grantType);
    }

    @Override
    public void validParameters(MultiValueMap<String, String> parameters) {


    }
}
