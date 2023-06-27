package com.tyr.sas.support.authorication.gitee;

import com.tyr.sas.support.authorication.TyrAbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

public class TyrGiteeAuthenticationToken extends TyrAbstractAuthenticationToken {

    /**
     * Creates a token with the supplied array of authorities.
     *
     */
    public TyrGiteeAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, Set<String> scopes, Map<String, Object> additionalParameters) {
        super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
    }
}
