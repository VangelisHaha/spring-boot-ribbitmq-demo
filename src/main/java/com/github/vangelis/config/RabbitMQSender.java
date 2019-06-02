package com.github.vangelis.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * RabbitMQ封装发送消息
 *
 * @author Vangelis
 * @date 2019-06-02 11:56
 */

@AllArgsConstructor
@Slf4j
@Component
public class RabbitMQSender  implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback{
    private RabbitTemplate rabbitTemplate;
    @PostConstruct
    private void init() {
        // 使用当前类作为发送后回调通知
        rabbitTemplate.setConfirmCallback(this);
        //是否当前类作为返回失败错误处理
        rabbitTemplate.setReturnCallback(this);
    }
    /**
     * Confirmation callback.
     *
     * @param correlationData correlation data for the callback.
     * @param ack             true for ack, false for nack
     * @param cause           An optional cause, for nack, when available, otherwise null.
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("消息已经确认收到");
        }else {
            log.info("消息没有确认收到，你该在这里写没有收到消息的处理代码");
        }

    }

    /**
     *   开启事务发送消息的封装
     * @param exchange  exchange
     * @param routingKey routingKey
     * @param msg  消息体
     */
    @Transactional(rollbackFor = Exception.class)
    public void sendMsg(String exchange,String routingKey,Object msg) {
        log.info("使用事务发送消息，msg{}", msg);
        rabbitTemplate.convertAndSend(exchange,routingKey,msg);
    }

    /**
     * Returned message callback.
     *
     * @param message    the returned message.
     * @param replyCode  the reply code.
     * @param replyText  the reply text.
     * @param exchange   the exchange.
     * @param routingKey the routing key.
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息返回没有收到");
    }
}
