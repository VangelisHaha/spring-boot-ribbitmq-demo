package com.github.vangelis.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vangelis.model.User;
import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * 监听器消费消息
 *
 * @author Vangelis
 * @date 2019-06-01 21:14
 */
@Component
@AllArgsConstructor
@RabbitListener(queues = "message")
public class MQListener implements ChannelAwareMessageListener {
    private final ObjectMapper objectMapper;
    /**
     * Callback for processing a received Rabbit message.
     * @throws Exception Any.
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
        byte[] body = message.getBody();
        User user = objectMapper.readValue(body, User.class);
        channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            channel.basicReject(deliveryTag, false);
            e.printStackTrace();
        }
    }
}
