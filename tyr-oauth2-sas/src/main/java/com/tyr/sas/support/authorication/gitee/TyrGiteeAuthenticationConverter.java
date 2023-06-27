package com.tyr.sas.support.authorication.gitee;

import com.tyr.sas.support.authorication.TyrAbstractAuthenticationConverter;
import com.tyr.sas.support.authorication.TyrAbstractAuthenticationToken;
import com.tyr.sas.support.constant.SecurityConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

public class TyrGiteeAuthenticationConverter extends TyrAbstractAuthenticationConverter<TyrGiteeAuthenticationToken> {

    @Override
    public TyrGiteeAuthenticationToken getAuthenticationToken(Authentication clientPrincipal, Set requestedScopes, Map additionalParameters) {
        return new TyrGiteeAuthenticationToken(new AuthorizationGrantType(SecurityConstants.GITEE),clientPrincipal,requestedScopes,additionalParameters);
    }

    @Override
    public boolean support(String grantType) {
        return SecurityConstants.GITEE.equals(grantType);
    }

    @Override
    public void validParameters(MultiValueMap<String, String> parameters) {


    }
}
