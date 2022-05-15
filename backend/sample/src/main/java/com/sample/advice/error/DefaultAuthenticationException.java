package com.sample.advice.error;

import com.sample.advice.payload.ErrorCode;

import org.springframework.security.core.AuthenticationException;

import lombok.Getter;


@Getter
public class DefaultAuthenticationException extends AuthenticationException{

    private ErrorCode errorCode;

    public DefaultAuthenticationException(String msg, Throwable t) {
        super(msg, t);
        this.errorCode = ErrorCode.INVALID_REPRESENTATION;
    }

    public DefaultAuthenticationException(String msg) {
        super(msg);
    }

    public DefaultAuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
