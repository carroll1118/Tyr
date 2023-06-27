package com.tyr.security.support.exception;

import org.springframework.security.core.AuthenticationException;

public class MailCodeException extends AuthenticationException {

    public MailCodeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public MailCodeException(String msg) {
        super(msg);
    }
}