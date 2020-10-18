package com.rrk.common.constant;

/**
 * 数字常量
 */
public class NumConstants {

    /**
     * 抢单用户500以下开3线程去抢单
     */
    public static final Integer THREAD_THREE = 3;

    /**
     * 用户500到1000之间 5个线程
     */
    public static final Integer THREAD_FIVE = 5;

    /**
     * 1000到2000 采用 8个线程
     */
    public static final Integer THREAD_AGE = 8;

    /**
     * 2000到5000用户 12
     */
    public static final Integer THREAD_TWELVE = 12;

    /**
     * 5000以上 15个线程
     */
    public static final Integer THREAD_FIFTEEN =15;

    /**
     * 500用户
     */
    public static final Integer USER_COUNT_FIVE_HUNDRED = 500;
    /**
     * 1000用户
     */
    public static final Integer USER_COUNT_ONE_THOUSAND =1000;

    /**
     * 2000
     */
    public static final Integer USER_COUNT_TWO_THOUSAND =2000;

    /**
     * 5000
     */
    public static final Integer USER_COUNT_FIVE_THOUSAND = 5000;

    /**
     * 30分钟未支付 自动取消订单
     */
    public static final Integer KILL_ORDERE_CANCEL = 30*60*1000;

    /**
     * 过滤器执行的顺序
     */
    public static final Integer GATEWAY_SORT_FIRST = 0;

    /**
     *
     */
    public static final Integer GATEWAY_SORT_SECOND = 1;

    /**
     * 设置短信过期时间为5分钟
     */
    public static final Integer SMS_EXPIRE = 5;

    /**
     * 后台用户
     */
    public static final Integer MANAGE_USER = 0;

    /**
     * 移动端用户
     */
    public static final Integer APP_USER = 1;

    /**
     * 用户来源（手机）
     */
    public static final  Integer SOURCE_MOBILE = 0;

    /**
     * 用户来源（微博）
     */
    public static final  Integer SOURCE_WEIBO = 1;
    /**
     * 来源微信
     */
    public static final Integer SOURCE_WEIXIN = 2;

    /**
     * 用户来源（QQ）
     */
    public static final  Integer SOURCE_QQ = 2;
    /**
     * 用户来源（支付宝）
     */
    public static final  Integer SOURCE_ALIPAY = 3;




}
