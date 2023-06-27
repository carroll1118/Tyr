package com.tyr.sas.support.authorication;

import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.*;

/**
 * 自定义身份验证对象的抽象类。所有自定义AuthenticationToken都继承此类
 *
 */
public abstract class TyrAbstractAuthenticationToken extends AbstractAuthenticationToken {

    @Getter
    private final AuthorizationGrantType authorizationGrantType;

    @Getter
    private final Authentication clientPrincipal;

    @Getter
    private final Set<String> scopes;

    @Getter
    private final Map<String, Object> additionalParameters;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorizationGrantType
     * @param clientPrincipal
     * @param scopes
     * @param additionalParameters
     */
    public TyrAbstractAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal,
                                          @Nullable Set<String> scopes, @Nullable  Map<String, Object> additionalParameters) {
        super(Collections.emptyList());
        this.authorizationGrantType = authorizationGrantType;
        this.clientPrincipal = clientPrincipal;
        this.scopes = Collections.unmodifiableSet(scopes != null ? new HashSet<>(scopes) : Collections.emptySet());;
        this.additionalParameters = Collections.unmodifiableMap(additionalParameters !=null ? new HashMap<>(additionalParameters) : Collections.emptyMap());
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return this.clientPrincipal;
    }
}