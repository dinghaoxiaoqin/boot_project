package com.rrk.oauth.config.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

/**
 *
 */
@Configuration
@Component
public class SocialAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    /**
     * 第三方授权登录
     * @param http
     */
    @Override
    public void configure(HttpSecurity http) {
        SocialAuthenticationProvicer provider = new SocialAuthenticationProvicer();
        http.authenticationProvider(provider);
    }
}
