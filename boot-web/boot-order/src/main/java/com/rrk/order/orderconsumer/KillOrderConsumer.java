package com.rrk.order.orderconsumer;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rrk.common.constant.MqConstatns;
import com.rrk.common.constant.RedisConstant;
import com.rrk.common.modules.order.dto.KillOrderVo;
import com.rrk.order.service.IOrderSkuService;
import com.rrk.order.utils.RabbitMqUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 秒杀订单支出超时处理
 */
@Component
@Slf4j
public class KillOrderConsumer implements ChannelAwareMessageListener {

    private Integer RETRY_COUNT = 0;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IOrderSkuService orderSkuService;

    @Autowired
    private RabbitMqUtil rabbitMqUtil;


    @RabbitListener(queues = MqConstatns.CANCEL_KILL_ORDER_QUEUE)
    @RabbitHandler
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        String msgStr = new String(message.getBody(), "UTF-8");
        Object correlation = message.getMessageProperties().getHeaders().get("spring_returned_message_correlation");
        Object mqData = redisTemplate.opsForHash().get(RedisConstant.KILL_QUEUE_KEY, correlation.toString());
        try {
            log.info("接收到要秒杀订单消息：msgStr->{}", msgStr);
            if (ObjectUtil.isNull(mqData)) {
                //说明已经消费成功，消息不重回队列（防止重发消息）
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
                return;
            }
            if (StrUtil.isNotBlank(msgStr)) {
                KillOrderVo orderVo = JSON.parseObject(msgStr, KillOrderVo.class);
                System.out.println("获取消息数据："+orderVo);
                //msgStr = msgStr.substring(0,msgStr.length()-1);
                //  库存要回滚orderCode+"_"+userId+"_"+skuId;
                //String[] strArr = msgStr.split("_");
                orderSkuService.addKillOrder(KillOrderVo.NO_PAY_ORDER,orderVo);
                //消费成功就删除redis中备份的数据
                redisTemplate.opsForHash().delete(RedisConstant.KILL_QUEUE_KEY, correlation.toString());
            }
            //牵手模式设置  默认自动应答模式  true:自动应答模式 false 手动接受 --》让消息队列删除队列的消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            //重复消费的次数
            RETRY_COUNT ++;
            log.error("取消秒杀订单重试的次数：retryCount->{}",RETRY_COUNT);
            log.info("获取取消秒杀订单出现异常：e->{}", e);
            throw new RuntimeException("重试消息失败：e->{}",e);
        } finally {
            if (RETRY_COUNT >= 3) {
                //将消费失败的消息加入死信队列
                rabbitMqUtil.sendDead(message);
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
                redisTemplate.opsForHash().delete(RedisConstant.KILL_QUEUE_KEY, correlation.toString());
            }
        }
    }
}
