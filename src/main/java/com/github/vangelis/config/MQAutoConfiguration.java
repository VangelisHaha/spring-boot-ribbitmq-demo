package com.github.vangelis.config;

import com.github.vangelis.consumer.MQListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
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
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //开启回调 必须设置为true
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData,ack,cause)->{
            //实现消息到达交换器的回调方法
            log.info("RabbitMQ sendMessage-->confirm-->start correlationData:{},ack:{},cause:{}",correlationData,ack,cause);


            log.info("RabbitMQ sendMessage-->confirm-->end");
        });
        rabbitTemplate.setReturnCallback((message,replyCode,replyText,exchange,routingKey)->{
            //实现消息到达队列的回调方法
            log.info("RabbitMQ sendMessage-->returnedMessage-->start message:{},replyCode:{},replyText:{},exchange:{},routingKey:{}"
                    ,message,replyCode,replyText,exchange,routingKey);


            log.info("RabbitMQ sendMessage-->returnedMessage-->end");
        });

        return rabbitTemplate;
    }

    /**
     *  创建message 队列
     * @return bean
     */
    @Bean
    public Queue messageQueue(){
        return new Queue("message",true);
    }

    /**
     *  创建 direct 交换器
     * @return bean
     */
    @Bean
    public  DirectExchange messageDirectExchange(){
        // 参数说明 交换器名字   是否持久化到硬盘  是否自动删除
        return new DirectExchange(" direct.exchange", true, false);
    }
    /**
     *  创建上方的绑定关系
     */
    @Bean
    public Binding messageExchangeBindingQueue(DirectExchange messageDirectExchange,Queue messageQueue){
        return BindingBuilder.bind(messageQueue).to(messageDirectExchange).with("message");
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

    private final MQListener mqListener;
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(Queue messageQueue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        // RabbitMQ默认是自动确认，这里改为手动确认消息
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setQueues(messageQueue);
        container.setMessageListener(mqListener);
        return container;
    }


    /**
     * 配置启用rabbitmq事务
     */
//    @Bean
//    public RabbitTransactionManager rabbitTransactionManager(CachingConnectionFactory connectionFactory) {
//        return new RabbitTransactionManager(connectionFactory);
//    }
}
