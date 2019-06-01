package com.github.vangelis.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 项目中Mq的自动配置
 *
 * @author Vangelis
 * @date 2019-06-01 20:36
 */
@Configuration
@Slf4j
@AllArgsConstructor
public class MQAutoConfiguration {
    private final ConnectionFactory connectionFactory;
    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory);
    }

    /**
     *  用于项目所需要的交换器和消息队列
     */
    @Bean
    RabbitAdmin rabbitAdmin() {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        //创建交换器
        DirectExchange messageExchange = new DirectExchange("交换器名字", true, false);
        admin.declareExchange(messageExchange);
        //创建 通知消息队列
        Queue messageTemplateCodeQueue = new Queue("队列名字");
        admin.declareQueue(messageTemplateCodeQueue);
        //创建绑定关系
        Binding voucherMessageBinding = BindingBuilder
                .bind(messageTemplateCodeQueue).to(messageExchange).with("路由key");
        admin.declareBinding(voucherMessageBinding);
        return admin;
    }

    /**
     *  监听器中默认封装实现
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        return factory;
    }
}
