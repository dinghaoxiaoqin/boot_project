package com.rrk.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author dinghao
 * @date 2020 6-28
 */
@Slf4j
public class MyAccountAuthentication extends AbstractAuthenticationToken {
    private Object credentials;
    private String principal;

    public MyAccountAuthentication(Object credentials, String principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.credentials = credentials;
        this.principal = principal;
    }
    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
