package com.rrk.oauth.config.exceptConfig;

import org.springframework.security.core.AuthenticationException;

/**
 * @author dinghao
 * @date 2020-6-25
 * 登录用户名输入错误的异常
 */

public class LoginNameException extends AuthenticationException {

    public LoginNameException(String msg) {
        super(msg);
    }
}
