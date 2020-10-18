package com.rrk.common.modules.commonConstant;

public class Constant {

    public final static String AUTHORIZATION = "Authorization";
    /**
     * 登录刷新时间
     */
    public final static String RENEWAL_TIME = "renewalTime";

    public final static String TOKEN = "token";
    public final static String NO_LOGIN = "您目前还没有登陆";
    public final static String TOKEN_CHECK_ERROR = "token校验出错";
    public final static String TOKEN_EXPIRED = "登陆token已经失效";
    public final static String USER_LOGIN_AGAIN = "您已登录";
    public final static String BEARER = "Bearer ";

    /**
     * token过期时间,3天
     */
    public final static Integer TOKEN_THREE_SECORD_EXPIRE = 3*24*60*60;


    /**
     * 密码加密相关
     */
    public static String SALT = "dinghao";
    public static final int HASH_ITERATIONS = 1;

}
