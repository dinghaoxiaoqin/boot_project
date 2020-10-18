package com.rrk.oauth.config.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

/**
 * @author dinghao
 * 手机短信验证码登录的配置
 */
@Configuration
@Component
public class SmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    /**
     * 手机短信登录
     * @param http
     */
    @Override
    public void configure(HttpSecurity http) {
        SmsCodeAuthenticationProvicer provider = new SmsCodeAuthenticationProvicer();
        http.authenticationProvider(provider);
    }

}
