package com.tyr.sas.support.authorication.sms;

import com.tyr.sas.support.authorication.TyrAbstractAuthenticationProvider;
import com.tyr.sas.support.constant.SecurityConstants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.CollectionUtils;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ERROR_URI;

public class TrySmsCodeAuthenticationProvider extends TyrAbstractAuthenticationProvider<TrySmsCodeAuthenticationToken> {

    public TrySmsCodeAuthenticationProvider(AuthenticationManager authenticationManager, OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        super(authenticationManager, authorizationService, tokenGenerator);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TrySmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public void checkClient(RegisteredClient registeredClient) {
        if (registeredClient != null && !registeredClient.getAuthorizationGrantTypes().contains(new AuthorizationGrantType(SecurityConstants.SMS))) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }
    }

    @Override
    public Authentication getAuthentication(Map<String, Object> reqParameters) {

        checkSmsCode(reqParameters);

        String phone = (String) reqParameters.get(SecurityConstants.MOBILE);
        String smsCode = (String) reqParameters.get(SecurityConstants.SMS_CODE);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(phone, smsCode);
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }


    private void checkSmsCode(Map<String, Object> parameters) {
        // 请求头中取出验证码  然后与发送的验证码校验
        String smsCode = (String) parameters.get(SecurityConstants.SMS_CODE);
        System.out.println(smsCode);

    }


}
