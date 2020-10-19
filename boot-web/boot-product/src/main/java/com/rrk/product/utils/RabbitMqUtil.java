package com.rrk.product.utils;

import com.alibaba.fastjson.JSON;
import com.rrk.common.constant.MqConstatns;
import com.rrk.common.dto.UserActionEntity;
import com.rrk.common.modules.product.dto.webdto.ProductDetailDto;
import com.rrk.product.entity.EsEntity;
import com.rrk.product.entity.HotWordEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
     * 用户行为分析
     * @param skuId
     * @param userId
     * @param productDetailDto
     */
    public void sendUserMessage(Long skuId, Long userId, ProductDetailDto productDetailDto) {
        try {
            //1.将商品和用户id封装
            UserActionEntity userActionEntity = new UserActionEntity(userId,skuId,productDetailDto);
            log.info("用户访问商品详情数据：userActionEntity->{}",userActionEntity);
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
            log.error("用户访问商品详情消息发送失败：e->{}",e);
        }
    }

    /**
     * 热搜词到es的index文档
     * @param esEntity
     */
    public void sendHotWord(EsEntity esEntity) {
        try {
            HotWordEntity hotWordEntity = new HotWordEntity(esEntity);
            log.info("即将要发送到mq的热搜词数据：hotWordEntity->{}",hotWordEntity);
            Message build = MessageBuilder.withBody(JSON.toJSONString(hotWordEntity)
                    .getBytes()).setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .build();
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend(MqConstatns.ES_EXCHANGE,MqConstatns.HOT_WORD_ROUTING,build,correlationData);
        } catch (Exception e){
            log.error("同步热搜词到es文档：e->{}",e);
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
