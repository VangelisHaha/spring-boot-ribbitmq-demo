package com.github.vangelis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRibbitmqDemoApplicationTests {
    private RabbitTemplate rabbitTemplate;

    @Test
    public void contextLoads() {
        rabbitTemplate.convertAndSend("路由key","消息内容");
        rabbitTemplate.convertAndSend("消息内容");
        rabbitTemplate.convertAndSend("交换器","路由key","消息内容");
    }

}
