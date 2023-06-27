package com.tyr.sas.support.authorication.mail;

import com.tyr.sas.support.authorication.TyrAbstractAuthenticationProvider;
import com.tyr.sas.support.constant.SecurityConstants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.Map;

public class TryMailAuthenticationProvider extends TyrAbstractAuthenticationProvider<TryMailAuthenticationToken> {

    public TryMailAuthenticationProvider(AuthenticationManager authenticationManager, OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        super(authenticationManager, authorizationService, tokenGenerator);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TryMailAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public void checkClient(RegisteredClient registeredClient) {
        if (registeredClient != null && !registeredClient.getAuthorizationGrantTypes().contains(new AuthorizationGrantType(SecurityConstants.MAIL))) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }
    }

    @Override
    public Authentication getAuthentication(Map<String, Object> reqParameters) {

        checkSmsCode(reqParameters);

        String mail = (String) reqParameters.get(SecurityConstants.MAILBOX);
        String mailCode = (String) reqParameters.get(SecurityConstants.MAIL_CODE);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(mail, mailCode);
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }


    private void checkSmsCode(Map<String, Object> parameters) {
        // 请求头中取出验证码  然后与发送的验证码校验
        String mailCode = (String) parameters.get(SecurityConstants.MAIL_CODE);
        System.out.println(mailCode);

    }


}
