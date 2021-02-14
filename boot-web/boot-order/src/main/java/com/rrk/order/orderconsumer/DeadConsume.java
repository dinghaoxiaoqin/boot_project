package com.rrk.order.orderconsumer;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rrk.common.constant.MqConstatns;
import com.rrk.common.modules.order.dto.KillOrderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeadConsume implements ChannelAwareMessageListener {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RabbitListener(queues = MqConstatns.DEAD_QUEUE)
    @RabbitHandler
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        String msgStr = new String(message.getBody(), "UTF-8");
        Object correlation = message.getMessageProperties().getHeaders().get("spring_returned_message_correlation");
        System.out.println("correlation" + correlation);
        System.out.println("收到进入死信队列消息：" + msgStr);
        KillOrderVo orderVo = JSON.parseObject(msgStr, KillOrderVo.class);
        //死信消息加入
        redisTemplate.opsForHash().put("dead_data", orderVo.getOrderNo().toString(),msgStr);
        //确认消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);

    }
}
