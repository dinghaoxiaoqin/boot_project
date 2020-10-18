package com.rrk.oauth.config.auth;

import cn.hutool.core.util.ObjectUtil;
import com.rrk.common.handle.LoginException;
import com.rrk.oauth.config.tokenConfig.SmsCodeToken;
import com.rrk.oauth.config.tokenConfig.SocialAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * 自定义登录认证管理器
 */
@Component
@Slf4j
public class MyAuthenticationManager implements AuthenticationManager {


    @Autowired
    private MyAuthenticationProvicer myAuthenticationProvicer;

    @Autowired
    private SmsCodeAuthenticationProvicer smsCodeAuthenticationProvicer;

    @Autowired
    private SocialAuthenticationProvicer socialAuthenticationProvicer;

    /**
     * 账户密码登录的认证管理
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication authenticate = myAuthenticationProvicer.authenticate(authentication);
        if (ObjectUtil.isNotNull(authenticate)) {
            return authenticate;
        }
       throw new LoginException(436,"登录失败");
    }

    /**
     * 短信验证码的登录认证管理
     * @param smsCodeToken
     * @return
     */
    public Authentication appAuthenticate(SmsCodeToken smsCodeToken) {
        Authentication authenticate = smsCodeAuthenticationProvicer.authenticate(smsCodeToken);
        if (ObjectUtil.isNotNull(authenticate)) {
            return authenticate;
        }
        throw new LoginException(436,"登录失败");
    }

    /**
     * 第三方社交登录的认证管理
     */
    public Authentication socialAuthenticate(SocialAuthenticationToken socialAuthenticationToken){
         Authentication authenticate = socialAuthenticationProvicer.authenticate(socialAuthenticationToken);
        if (ObjectUtil.isNotNull(authenticate)) {
            return authenticate;
        }
        throw new LoginException(436,"登录失败");
    }
}
