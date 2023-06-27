package com.tyr.sas.support.authorication.sms;

import com.tyr.sas.support.authorication.TyrAbstractAuthenticationConverter;
import com.tyr.sas.support.constant.SecurityConstants;
import com.tyr.sas.support.utils.OAuth2EndpointUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class TrySmsCodeAuthenticationConverter extends TyrAbstractAuthenticationConverter<TrySmsCodeAuthenticationToken> {

    @Override
    public TrySmsCodeAuthenticationToken getAuthenticationToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters) {
        return new TrySmsCodeAuthenticationToken(new AuthorizationGrantType(SecurityConstants.SMS),clientPrincipal,requestedScopes,additionalParameters);
    }

    @Override
    public boolean support(String grantType) {
        return SecurityConstants.SMS.equals(grantType);
    }

    @Override
    public void validParameters(MultiValueMap<String, String> parameters) {


    }
}
