package com.rrk.product.mqConsumer;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rrk.common.constant.ElasticsearchContants;
import com.rrk.common.constant.MqConstatns;
import com.rrk.common.dto.UserActionEntity;
import com.rrk.common.utils.ToolUtil;
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
 * @author dinghao
 * @date 2020-8-25
 * 用户行为消费者
 *
 */
@Slf4j
@Component
public class UserActionConsumer implements ChannelAwareMessageListener {

    /**
     * 接收消息将用户的访问数据同步到es的index
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(queues = MqConstatns.USER_ACTION_QUEUE)
    @RabbitHandler
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            String msgStr = new String(message.getBody(), "UTF-8");
            log.info("接收用户行为消息：msgStr->{}",msgStr);
            if (StrUtil.isNotBlank(msgStr)) {
                UserActionEntity userActionEntity = JSON.parseObject(msgStr, UserActionEntity.class);
                Long id = ToolUtil.getLongNum();
                XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
                        .startObject()
                        .field("id", id)
                        .field("userid",userActionEntity.getUserId())
                        .field("productname", userActionEntity.getProductName())
                        .field("operatetype",userActionEntity.getOperateType())
                        //.field("createtime", DateUtils.formatDate(userActionEntity.getCreateTime(),"yyyy-MM-dd HH:mm:ss"))
                        .field("createtime", userActionEntity.getCreateTime())
                        .endObject();
                String s = ElasticsearchUtil.addData(xContentBuilder, ElasticsearchContants.USER_ACTION_INDEX, id.toString());
                log.info("添加用户行为文档成功：s->{}",s);
                //牵手模式设置  默认自动应答模式  true:自动应答模式 false 手动接受 --》让消息队列删除队列的消息
                 channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        } catch (UnsupportedEncodingException e) {
            log.error("用户信息消费出现异常：e->{},mesStr->{}",e, new String(message.getBody(), "UTF-8") );
            if (message.getMessageProperties().getRedelivered()) {
                log.error("用户行为信息已重复处理失败,拒绝再次接收...:msgStr->{}", new String(message.getBody(), "UTF-8"));
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
