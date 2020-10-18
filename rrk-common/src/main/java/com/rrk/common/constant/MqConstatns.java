package com.rrk.common.constant;

/**
 * mq队列的相关常量
 */
public class MqConstatns {
    /**
     * 简单测试队列
     */
    public static final String QUEUE_TEST = "queue_test";

    /**
     * 交换机模式
     */
    public static final String EXCHANGE_TEST = "exchange_test";

    /**
     * 路由键
     */
    public static final String REDIRCT_TEST = "redirct_test";

    /**
     * 创建发送红包的队列
     */
    public static final String REDPACKET_QUEUE = "redpacket_queue";

    /**
     * 创建红包交换机
     */
    public static final String REDPACKET_EXCHANGE = "redpacket_exchange";

    /**
     * 创建绑定红包的路由键
     */
    public static final String REDPACKET_ROUTEKEY = "redpacket_routekey";

    /**
     * 创建红包记录的延时队列
     */
    public static final String RED_USER_DEAD_QUEUE = "red_user_dead_queue";
    /**
     * 绑定延迟对列的路由键
     */
    public static final String RED_DEAD_ROUTEKEY = "red_dead_routekey";

    /**
     * 红包延迟交换机
     */
    public static final String RED_DEAD_EXCHANGE = "red_dead_exchange";

    /**
     * 原红包结果的统计队列
     */
    public static final String RED_PACKET_QUEUE = "red_pack_queue";

    /**
     * 路由键
     */
    public static final String RED_PACKET_ROUTING = "red_packet_routing";

    /**
     * 订单交换机
     */
    public static final String KILL_ORDER_EXCHANGE = "kill_order_exchange";

    /**
     * 秒杀下单队列
     */
    public static final String KILL_ORDER_QUEUE = "kill_order_queue";

    /**
     * 秒杀订单路由键
     */
    public static final String KILL_ORDER_ROUTING = "kill_order_routing";

    /**
     * 商品交换机
     */
    public static final String PRODUCT_EXCHANGE = "product_exchange";
    /**
     * 参与商品秒杀的路由键
     */
    public static final String KILL_PRODUCT_ROUTING = "kill_product_routing";

    /**
     * 参与秒杀商品的队列
     */
    public static final String KILL_PRODUCT_QUEUE = "kill_product_queue";

    /**
     * 存放到es搜索词队列
     */
    public static final String HOT_WORD_QUEUE = "hot_word_queue";
    /**
     * 同步用户行为的队列名称
     */
    public static final String USER_ACTION_QUEUE = "user_action_queue";
    /**
     * 同步es的交换机
     */
    public static final String ES_EXCHANGE = "es_exchange";
    /**
     * 绑定搜索词的路由键
     */
    public static final String HOT_WORD_ROUTING = "hot_word_routing";
    /**
     * 绑定用户行为的路由键
     */
    public static final String USER_ACTION_ROUTING = "user_action_routing";

    /**
     * 订单的延迟队列交换机
     */
    public static final String CANCEL_ORDER_EXCHANGE = "cancel_order_exchange";

    /**
     * 取消订单的延迟队列
     */
    public static final String CANCEL_ORDER_QUEUE = "cancel_order_queue";

    /**
     * 取消订单的延迟路由
     */
    public static final String CANCEL_ORDER_ROUTING = "cancel_order_routing";

}
