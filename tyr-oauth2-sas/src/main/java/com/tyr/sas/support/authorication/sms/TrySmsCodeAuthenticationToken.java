package com.tyr.sas.support.authorication.sms;

import com.tyr.sas.support.authorication.TyrAbstractAuthenticationToken;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class TrySmsCodeAuthenticationToken extends TyrAbstractAuthenticationToken {

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorizationGrantType
     * @param clientPrincipal
     * @param scopes
     * @param additionalParameters
     */
    public TrySmsCodeAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, Set<String> scopes, Map<String, Object> additionalParameters) {
        super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
    }
}
