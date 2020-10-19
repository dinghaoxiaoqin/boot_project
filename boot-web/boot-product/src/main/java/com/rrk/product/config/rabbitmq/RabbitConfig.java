package com.rrk.product.config.rabbitmq;

import com.rrk.common.constant.MqConstatns;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * 同步到es的队列信息
 */
@Configuration
@Slf4j
public class RabbitConfig {

    /**
     * 同步es的交换机
     */
    @Bean
    public Exchange esExchange(){
        return ExchangeBuilder.directExchange(MqConstatns.ES_EXCHANGE)
                .durable(true) //交换机持久化
                .build();
    }

    /**
     * 同步搜索词的队列
     */
    @Bean
    public Queue esHotWordQueue(){
        //队列持久化
        Queue queue = new Queue(MqConstatns.HOT_WORD_QUEUE, true);
        queue.setShouldDeclare(true);
        return queue;
    }
    /**
     * 用户行为分析的队列
     */
    @Bean
    public Queue esUserQueue(){
        Queue queue = new Queue(MqConstatns.USER_ACTION_QUEUE, true);
        queue.setShouldDeclare(true);
        return queue;
    }
    /**
     * 将搜索词的队列绑定到交换机
     */
    @Bean
    public Binding hotwordBinding(){
        return BindingBuilder
                .bind(esHotWordQueue())
                .to(esExchange())
                .with(MqConstatns.HOT_WORD_ROUTING).noargs();
    }
    /**
     * 将用户行为绑定交换机
     */
    @Bean
    public Binding userActionBinding(){
        return BindingBuilder.bind(esUserQueue())
                .to(esExchange())
                .with(MqConstatns.USER_ACTION_ROUTING).noargs();
    }
}
