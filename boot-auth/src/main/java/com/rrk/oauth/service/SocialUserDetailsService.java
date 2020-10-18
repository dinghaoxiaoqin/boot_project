package com.rrk.oauth.service;

import com.rrk.oauth.config.userConfig.UserSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author dinghao
 * @date 2020-7-30
 * 通过第三方提供的授权码进行登录的接口
 */
public interface SocialUserDetailsService extends UserDetailsService {
    /**
     * 通过认证授权码进行登录
     * @param
     * @return
     */
    UserSecurity loadUserByAuthorizationCode (String authorizationCode,Integer id);
}
