package com.rrk.order.utils;

import com.alibaba.fastjson.JSON;
import com.rrk.common.constant.MqConstatns;
import com.rrk.common.constant.OrderContants;
import com.rrk.common.dto.UserActionEntity;
import com.rrk.common.handle.OrderException;
import com.rrk.common.modules.order.dto.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * mq的工具类
 */
@Slf4j
@Component
public class RabbitMqUtil implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback{


    @Autowired
    private RabbitTemplate rabbitTemplate;



    /**
     * 超时自动取消订单
     * @param orderDto
     */
    public void orderAddQueue(OrderDto orderDto) {
        log.info("下单成功进入延迟队列的数据：orderDro->{}",orderDto);
        try {
            Message build = MessageBuilder.withBody(JSON.toJSONString(orderDto).getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .build();
            //唯一标识id
            String id = UUID.randomUUID().toString();
            CorrelationData correlationData = new CorrelationData(id);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            log.info("发送消息的时间：" + format.format(new Date()));
            rabbitTemplate.convertAndSend(MqConstatns.CANCEL_ORDER_EXCHANGE, MqConstatns.CANCEL_ORDER_ROUTING, (Object) build, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    //延迟半个小时
                    message.getMessageProperties().setHeader("x-delay", OrderContants.KILL_ORDER_OVER*60*1000);
                    return message;
                }
            },correlationData);

        } catch (Exception e){
            log.error("下单发送到延迟队列消息失败：e->{}，orderDto-》{}",e,orderDto);
            throw new OrderException(566,"下单发送到延迟队列消息失败");
        }
    }

    /**
     * 发送信息到es库存
     * @param userId
     * @param productName
     */
    public void sendUserMessage(Long userId, String productName) {
        try {
            //1.将商品和用户id封装
            UserActionEntity userActionEntity = new UserActionEntity(userId,productName);
            log.info("用户下单商品数据：userActionEntity->{}",userActionEntity);
            //2创建消息体
            Message build = MessageBuilder.withBody(JSON.toJSONString(userActionEntity)
                    .getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .build();
            //3,保证幂等性（给唯一标识id）
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            //4,发送消息
            rabbitTemplate.convertAndSend(MqConstatns.ES_EXCHANGE,MqConstatns.USER_ACTION_ROUTING,build,correlationData);
        } catch (Exception e) {
            log.error("用户下单商品消息发送失败：e->{}",e);
        }
    }

    /**
     * 确定消息是否发送到交换机
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("确认发送的消息id:" + correlationData.getId());
        //如果消息没有到exchange
        if (ack) {
            log.info("消息到达交换机：ack->{}",ack);
        } else {
            log.error("消息发送确认失败的原因:" + cause);
            //可以从发一次
        }
    }

    /**
     *交换机的消息路由不到队列时触发（交换机消息丢失）
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息主体message->{}"+message);
        log.info("消息主体replyCode->{}"+replyCode);
        log.info("描述replyText->{}"+replyText);
        log.info("消息使用的交换机->{}"+exchange);
        log.info("消息使用的路由键->{}",routingKey);
    }


}
