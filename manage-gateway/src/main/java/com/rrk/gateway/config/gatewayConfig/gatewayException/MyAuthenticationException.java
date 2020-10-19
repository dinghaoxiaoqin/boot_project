package com.rrk.gateway.config.gatewayConfig.gatewayException;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ResponseStatusException;

/**
 * 认证管理的异常处理
 */
public class MyAuthenticationException extends ResponseStatusException {

    public MyAuthenticationException(@Nullable String reason) {
        super(HttpStatus.OK,reason);
    }
}
