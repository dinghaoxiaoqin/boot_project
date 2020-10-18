package com.rrk.common.constant;

/**
 * redis的相关常量
 */
public class RedisConstant {

    /**
     * token加入黑名单的key
     */
    public static final String BLACK_LIST ="black_list";
    /**
     * 保存用户token的大key
     */
    public static final String USER_TOKEN = "user_token";

    /**
     * 保存用户基本信息的key
     */
    public static final String USER_KEY = "user";

    /**
     * 存放菜单的基本信息
     */
    public static final String MENU_CACHE = "menu_list";
    /**
     * 员工的权限基本信息
     */
    public static final String PERMISSION_CACHE = "permission_list";
    /**
     * 员工的角色信息
     */
    public static final String PART_CACHE = "part_list";

    /**
     * 短信验证码大键
     */
    public static final String SMS_KEY = "sms-key";

    /**
     * 存放短信验证码信息(小健)
     */
    public static final String SMS_CODE = "sms_code";

    /**
     * 创建红包的外键
     */
    public static final String CREATE_RED = "create_red";

    /**
     * 存放秒杀商品的key
     */
    public static final String KILL_PRODUCT = "kill_product";

    /**
     * 存放参与秒杀的客户 和商品id外键
     */
    public static final String KILL_USER_PRODUCT = "kill_user_product";

    /**
     * 秒杀订单（未支付）的key
     */
    public static final String KILL_USER_ORDER = "kill_user_order";

    /**
     * 用户收货地址key
     */
    public static final String USER_ADDRESS_KEY = "user_address_key";
}
