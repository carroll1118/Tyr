package com.tyr.security.support.exception;

import org.springframework.security.core.AuthenticationException;

public class SmsCodeException extends AuthenticationException {
    public SmsCodeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SmsCodeException(String msg) {
        super(msg);
    }
}
