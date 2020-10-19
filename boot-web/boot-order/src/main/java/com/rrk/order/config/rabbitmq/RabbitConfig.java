package com.rrk.order.config.rabbitmq;

import com.rrk.common.constant.MqConstatns;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单的队列相关配置
 */
@Configuration
@Slf4j
public class RabbitConfig {

    /**
     * 同步es的交换机
     */
    @Bean
    public CustomExchange OrderDealyExchange(){
        Map<String, Object> args = new HashMap<>();
        //交换机的类型
        args.put("x-delayed-type","direct");
        return new CustomExchange(MqConstatns.CANCEL_ORDER_EXCHANGE, "x-delayed-message",true, false,args);
    }

    /**
     * 超时取消订单的队列
     */
    @Bean
    public Queue cancelOrderQueue(){
        Queue queue =new Queue(MqConstatns.CANCEL_ORDER_QUEUE,true);
        queue.setShouldDeclare(true);
        return queue;
    }

    /**
     * 取消订单的路由键
     */
    @Bean
    public Binding cancelOrderBinding(){
        return BindingBuilder
                .bind(cancelOrderQueue())
                .to(OrderDealyExchange())
                .with(MqConstatns.CANCEL_ORDER_ROUTING).noargs();
    }

}
