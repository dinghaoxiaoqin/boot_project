package com.rrk.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.HttpBasicServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 自定义认证管理过滤器
 */
@Component
@Slf4j
public class MyAuthenticationWebFilter extends AuthenticationWebFilter {
    /**
     * 前后台统一的token 标识
     */
    public static final String REQUEST_HEADER = "Authorization";
    @Autowired
    private MyServerAuthenticationConverter myServerAuthenticationConverter;

    @Autowired
    private MyReactiveAuthenticationManager myReactiveAuthenticationManager;

    private ServerWebExchangeMatcher requiresAuthenticationMatcher = ServerWebExchangeMatchers.anyExchange();

    private ServerAuthenticationFailureHandler authenticationFailureHandler = new ServerAuthenticationEntryPointFailureHandler(new HttpBasicServerAuthenticationEntryPoint());

    /**
     * Creates an instance
     *
     * @param authenticationManager the authentication manager to use
     */
    public MyAuthenticationWebFilter(MyReactiveAuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        return this.requiresAuthenticationMatcher.matches(exchange)
                .filter( matchResult -> matchResult.isMatch())
                .flatMap( matchResult -> this.myServerAuthenticationConverter.convert(exchange))
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap( token -> authenticate(exchange, chain, token));
    }

    private Mono<Void> authenticate(ServerWebExchange exchange,
                                    WebFilterChain chain, Authentication token) {
        WebFilterExchange webFilterExchange = new WebFilterExchange(exchange, chain);
        return this.myReactiveAuthenticationManager.authenticate(token)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new IllegalStateException("No provider found for " + token.getClass()))))
                .flatMap(authentication -> onAuthenticationSuccess(authentication, webFilterExchange))
                .onErrorResume(AuthenticationException.class, e -> this.authenticationFailureHandler
                        .onAuthenticationFailure(webFilterExchange, e));
    }

    public void setMyServerAuthenticationConverter(
            MyServerAuthenticationConverter myServerAuthenticationConverter) {
        Assert.notNull(myServerAuthenticationConverter, "authenticationConverter cannot be null");
        this.myServerAuthenticationConverter = myServerAuthenticationConverter;
    }
}
