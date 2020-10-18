package com.rrk.common.constant;

/**
 * 订单的相关常量
 */
public class OrderContants {
    /**
     * 普通订单
     */
    public static final Integer PO_ORDER_TYPE = 0;

    /**
     * 秒杀订单
     */
    public static final Integer KILL_ORDER_TYPE = 1;

    /**
     * 普通订单超时未支付的时间为1天
     */
    public static final Integer  PO_ORDER_OVER = 1;

    /**
     * 秒杀订单超时未支付的时间为半个小时
     */
    public static final Integer KILL_ORDER_OVER = 30;

    /**
     * 未支付的订单
     */
    public static final Integer WAIT_ORDER_STATUS = 0;

    /**
     * 待发货的状态
     */
    public static final Integer WAIT_SEND_STATUS = 1;
    /**
     * 已取消的状态
     */
    public static final Integer CANCEL_ORDER_STATUS = 4;


}
