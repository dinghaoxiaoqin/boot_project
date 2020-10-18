package com.rrk.oauth.config.auth;

import cn.hutool.core.util.ObjectUtil;
import com.rrk.common.handle.LoginException;
import com.rrk.oauth.config.tokenConfig.SmsCodeToken;
import com.rrk.oauth.config.userConfig.UserSecurity;
import com.rrk.oauth.service.SmsCodeUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @author dinghao
 * @date 2020-7-30
 * 短信验证码认证管理
 */
@Component
@Slf4j
public class SmsCodeAuthenticationProvicer implements AuthenticationProvider {

    @Autowired
    private SmsCodeUserDetailsService smsCodeUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeToken authenticationToken = (SmsCodeToken) authentication;
        String mobile = (String) authenticationToken.getPrincipal();
        UserSecurity userSecurity = smsCodeUserDetailsService.loadUserByMobile(mobile);
        if (ObjectUtil.isNull(userSecurity)) {
            throw new LoginException(437, "账户不存在，请重新输入");
        }
        return new SmsCodeToken(userSecurity);
    }

    @Override
    public boolean supports(Class<?> authentication) {

        return authentication.equals(SmsCodeToken.class);
    }
}
