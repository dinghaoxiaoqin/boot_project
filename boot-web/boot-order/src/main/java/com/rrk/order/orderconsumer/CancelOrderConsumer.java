package com.rrk.order.orderconsumer;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.rabbitmq.client.Channel;
import com.rrk.common.constant.MqConstatns;
import com.rrk.common.modules.order.dto.OrderDto;
import com.rrk.common.modules.order.entity.TbOrder;
import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.order.fegin.ProductFeginClient;
import com.rrk.order.service.ITbOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 自动取消未支付的订单
 */
@Component
@Slf4j
public class CancelOrderConsumer implements ChannelAwareMessageListener {

    @Autowired
    private ITbOrderService orderService;

    @Autowired
    private ProductFeginClient productFeginClient;

    /**
     * 监听自动取消订单
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    //@LcnTransaction(propagation = DTXPropagation.REQUIRED) //事务发起方
    @RabbitListener(queues = MqConstatns.CANCEL_ORDER_QUEUE)
    @RabbitHandler
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            String msgStr = new String(message.getBody(), "UTF-8");
            log.info("接收到要取消订单消息：msgStr->{}", msgStr);
            if (StrUtil.isNotBlank(msgStr)) {
                //修改订单的状态 和 库存要回滚
                OrderDto orderDto = JSON.parseObject(msgStr, OrderDto.class);
                //修改订单状态
                TbOrder tbOrder = new TbOrder();
                tbOrder.setOrderStatus(4);
                tbOrder.setOrderNo(orderDto.getOrderNo());
                orderService.update(tbOrder, new QueryWrapper<TbOrder>().eq("order_no", orderDto.getOrderNo()));
                //2.库存回滚
                TbSku sku = productFeginClient.getSkuById(orderDto.getSkuId());
                if (sku.getSaleCount() - orderDto.getNum() >= 0) {
                    sku.setStock(sku.getStock() + orderDto.getNum());
                    sku.setSaleCount((sku.getSaleCount() == null ? 0 : sku.getSaleCount()) - orderDto.getNum());
                    productFeginClient.updateStock(sku);
                }
            }
            //牵手模式设置  默认自动应答模式  true:自动应答模式 false 手动接受 --》让消息队列删除队列的消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("获取取消订单出现异常：e->{}", e.getMessage());
            if (message.getMessageProperties().getRedelivered()) {
                log.error("取消订单消息已重复处理失败,拒绝再次接收...");
                // 拒绝消息
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
            } else {
                log.error("取消订单消息即将再次返回队列处理...");
                // requeue为是否重新回到队列
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
        }

    }
}
