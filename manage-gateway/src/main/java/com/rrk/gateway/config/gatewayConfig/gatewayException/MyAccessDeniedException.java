package com.rrk.gateway.config.gatewayConfig.gatewayException;

import io.micrometer.core.lang.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * 授权认证异常
 */

public class MyAccessDeniedException extends ResponseStatusException {

    public MyAccessDeniedException(@Nullable String reason) {
        super(HttpStatus.OK, reason);
    }
}
