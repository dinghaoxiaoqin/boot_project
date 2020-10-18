package com.rrk.oauth.config.auth;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.rrk.common.handle.LoginException;
import com.rrk.oauth.config.tokenConfig.SmsCodeToken;
import com.rrk.oauth.config.tokenConfig.SocialAuthenticationToken;
import com.rrk.oauth.config.userConfig.UserSecurity;
import com.rrk.oauth.service.SocialUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * 第三方社交登录认证处理器
 * @author dinghao
 */
@Component
@Slf4j
public class SocialAuthenticationProvicer implements AuthenticationProvider {

    @Autowired
    private SocialUserDetailsService socialUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SocialAuthenticationToken authenticationToken = (SocialAuthenticationToken) authentication;
        //第三方的授权码
        String authorizationCode = (String) authenticationToken.getPrincipal();
        //属于哪种社交登录方式的id
        Integer socialId = Convert.toInt(authentication.getCredentials());
        UserSecurity userSecurity = socialUserDetailsService.loadUserByAuthorizationCode(authorizationCode,socialId);
        if (ObjectUtil.isNull(userSecurity)) {
            throw new LoginException(437, "账户不存在，请重新输入");
        }
        return new SmsCodeToken(userSecurity);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(SocialAuthenticationToken.class);
    }
}
