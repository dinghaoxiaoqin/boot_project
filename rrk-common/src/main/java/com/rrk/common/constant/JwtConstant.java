package com.rrk.common.constant;

/**
 * jwt的相关常量
 */
public class JwtConstant {

    /**
     * 授权的路径
     */
    public static final String AUTHENTICATION_URL = "/auth";

    /**
     * 放到请求头里面
     */
    public static final String AUTHENTICATION = "authorization";

    /**
     * 过期时间
     */
    public static final Integer expiration = 60;

    /**
     *
     */
    public static final String SECRET = "com.dinghao.product";

    /**
     *
     */
    public static final String STARTSWITH = "Bearer ";
}
