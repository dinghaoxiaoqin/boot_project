package com.rrk.oauth.config.auth;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.rrk.common.handle.LoginException;
import com.rrk.oauth.config.tokenConfig.SmsCodeToken;
import com.rrk.oauth.config.userConfig.MyUserDetailService;
import com.rrk.oauth.config.userConfig.UserSecurity;
import com.rrk.oauth.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 真正就行认证管理的地方
 */
@Component
@Slf4j
public class MyAuthenticationProvicer implements AuthenticationProvider {

    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private RedisUtil tokenRedisUtil;



    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 这个获取表单输入中返回的用户名;
        String username = (String) authentication.getPrincipal();
        // 这个是表单中输入的密码；
        String password = (String) authentication.getCredentials();
         UserSecurity userSecurity = userDetailService.loadUserByUsername(username);
        if (ObjectUtil.isNull(userSecurity)) {
            throw new LoginException(437,"账户不存在，请重新输入");
        }
        BCryptPasswordEncoder en = new BCryptPasswordEncoder();
        if (StrUtil.isNotBlank(password)) {
            boolean matches = en.matches(password, userSecurity.getPassword());
            if (!matches) {
                throw new LoginException(437,"密码不正确，请重新输入！");
            }
        }
        return new UsernamePasswordAuthenticationToken(userSecurity, password, userSecurity.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    /**
     * 移动端短信登录
     * @param smsCodeToken
     * @return
     */
    public Authentication appAuthenticate(SmsCodeToken smsCodeToken) {
        String mobile = (String) smsCodeToken.getPrincipal();
        UserSecurity userSecurity = userDetailService.loadUserByMobile(mobile);
        if (ObjectUtil.isNull(userSecurity)) {
            throw new LoginException(437,"账户不存在，请重新输入");
        }
        return new SmsCodeToken(userSecurity);
    }
}
