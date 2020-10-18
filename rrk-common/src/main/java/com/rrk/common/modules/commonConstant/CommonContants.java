package com.rrk.common.modules.commonConstant;

/**
 * 自定义的各种常量
 */
public class CommonContants {

    /**
     * 账户密码和手机短信登录
     */
    public static final Integer SMS_OR_PASSWORD = 4;

    /**
     * 微博社交登录id
     */
    public static final int WEIBO_ID = 5;
    /**
     * 微信社交登录id
     */
    public static final int  WEICHAT_ID = 6;

    /**
     * oauth2（grant_type）登录方式（密码密码模式）
     */
    public static final String GRANT_TYPE_PASSWORD = "password";

    /**
     * secret
     */
    public static final String SECRET = "dinghao";

    /**
     * location
     */
    public static final String LOCATION = "classpath:/dinghao.jks";
    /**
     * alias
     */
    public static final String ALIAS = "dinghao";

    /**
     * 获取微博的access_token的地址
     */
    public static final String WEIBO_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";
    /**
     * 获取微信的access_token 的地址
     */
    public static final String WEIXIN_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    /**
     * 获取微博用户信息的地址
     */
    public static final String WEIBO_USER_URL = "https://api.weibo.com/2/users/show.json";
    /**
     * 获取微信用户信息的地址
     */
    public static final String WEIXIN_USER_URL = "https://api.weixin.qq.com/cgi-bin/user/info";
}