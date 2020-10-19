package com.rrk.product.mqConsumer;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rrk.common.constant.ElasticsearchContants;
import com.rrk.common.constant.MqConstatns;
import com.rrk.common.utils.DateUtils;
import com.rrk.common.utils.ToolUtil;
import com.rrk.product.entity.HotWordEntity;
import com.rrk.product.utils.ElasticsearchUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * 热词消费者
 */
@Slf4j
@Component
public class HotWordConsumer implements ChannelAwareMessageListener {

    @RabbitListener(queues = MqConstatns.HOT_WORD_QUEUE)
    @RabbitHandler
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            String msgStr = new String(message.getBody(), "UTF-8");
            log.info("接收热词数据消息：msgStr->{}",msgStr);
            if (StrUtil.isNotBlank(msgStr)) {
                HotWordEntity hotWordEntity = JSON.parseObject(msgStr, HotWordEntity.class);
                Long id = ToolUtil.getLongNum();
                XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
                        .startObject()
                        .field("id", id)
                        .field("sku_id",hotWordEntity.getSkuId())
                        .field("product_name", hotWordEntity.getProductName())
                        .field("price",hotWordEntity.getPrice())
                        .field("sale_price",hotWordEntity.getSalePrice())
                        .field("sale_count",hotWordEntity.getSaleCount())
                        .field("sku_image",hotWordEntity.getSkuImage())
                        .field("create_time", DateUtils.formatDate(hotWordEntity.getCreateTime(),"yyyy-MM-dd HH:mm:ss"))
                        .endObject();
                String s = ElasticsearchUtil.addData(xContentBuilder, ElasticsearchContants.SEARCH_WORD_INDEX, id.toString());
                log.info("添加热搜文档成功：s->{}",s);
                //牵手模式设置  默认自动应答模式  true:自动应答模式 false 手动接受 --》让消息队列删除队列的消息
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        } catch (UnsupportedEncodingException e) {
            log.error("热搜词消费出现异常：e->{},mesStr->{}",e, new String(message.getBody(), "UTF-8") );
            if (message.getMessageProperties().getRedelivered()) {
                log.error("热搜词已重复处理失败,拒绝再次接收...:msgStr->{}", new String(message.getBody(), "UTF-8"));
                // 拒绝消息
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
            } else {
                log.error("消息即将再次返回队列处理...:msgStr->{}", new String(message.getBody(), "UTF-8"));
                // requeue为是否重新回到队列
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
        }

    }

}
