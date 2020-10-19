package com.rrk.gateway.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static io.netty.handler.codec.http.cookie.CookieHeaderNames.MAX_AGE;

/**
 * 资源权限配置
 */
@EnableWebFluxSecurity
public class ResourceServerConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private MyAuthenticationWebFilter myAuthenticationWebFilter;

    @Autowired
    private MyReactiveAuthorizationManager myReactiveAuthorizationManager;




    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf().disable();

         //MyReactiveAuthenticationManager myReactiveAuthenticationManager = new MyReactiveAuthenticationManager();
        //认证过滤器
       // AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(myReactiveAuthenticationManager);
       // authenticationWebFilter.setServerAuthenticationConverter(new ServerBearerTokenAuthenticationConverter());
        http.addFilterAt(corsFilter(), SecurityWebFiltersOrder.CORS)
                .addFilterAt(myAuthenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        http.httpBasic().disable()
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .anyExchange().access(myReactiveAuthorizationManager);

        return http.build();
    }

    /**
     * 跨域配置
     */
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                HttpHeaders requestHeaders = request.getHeaders();
                ServerHttpResponse response = ctx.getResponse();
                HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();
                HttpHeaders headers = response.getHeaders();
                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin());
                headers.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders.getAccessControlRequestHeaders());
                if (requestMethod != null) {
                    headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
                }
                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
                headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
                headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx);
        };
    }


}
