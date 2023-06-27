package com.tyr.security.support.authentication.mail;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * 存放认证信息（包括未认证前的参数信息传递）
 */

public class MailAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    public MailAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        setAuthenticated(false);
    }

    public MailAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    public static MailAuthenticationToken unauthenticated(Object principal) {
        return new MailAuthenticationToken(principal);
    }

    public static MailAuthenticationToken authenticated(Object principal,
                                                           Collection<? extends GrantedAuthority> authorities) {
        return new MailAuthenticationToken(principal, authorities);
    }


    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated,
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
