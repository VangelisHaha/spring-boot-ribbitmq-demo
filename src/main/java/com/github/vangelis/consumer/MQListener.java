package com.github.vangelis.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 监听器消费消息
 *
 * @author 王杰
 * @date 2019-06-01 21:14
 */
@Component
@RabbitListener(queues = "队列名")
public class MQListener {

    @RabbitHandler
    public void process(Object message) {
        System.out.println("consumer  : " + message);
    }
}
