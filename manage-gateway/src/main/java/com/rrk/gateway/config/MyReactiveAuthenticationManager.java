package com.rrk.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * token校验(认证管理器)
 */
@Component
@Slf4j
public class MyReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        log.info("获取的参数："+authentication);
        return Mono.justOrEmpty(authentication);
    }
}
