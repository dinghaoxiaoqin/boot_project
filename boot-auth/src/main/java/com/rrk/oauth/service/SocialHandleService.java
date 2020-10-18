package com.rrk.oauth.service;

import com.rrk.oauth.config.userConfig.UserSecurity;

/**
 * 提供一个处理不能类型社交登录的接口
 */
public interface SocialHandleService {

    /***
     * 第三方登录
     */
    UserSecurity getUserSecurity(Integer socialType,String authorizationCode);
}
