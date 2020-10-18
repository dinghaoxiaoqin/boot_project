package com.rrk.oauth.service;

import com.rrk.oauth.config.userConfig.UserSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 短信认证登录的业务接口
 * @author dinghao
 */
public interface SmsCodeUserDetailsService extends UserDetailsService {
    /**
     * 手机号认证
     * @param mobile
     * @return
     */
    UserSecurity loadUserByMobile (String mobile);
}
