package com.github.vangelis;

import com.github.vangelis.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRabbitMQDemoApplicationTests {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void contextLoads() {
        rabbitTemplate.convertAndSend("direct.123","message",User.builder().introduction("我是一个小毛驴").name("Vangelis").sex("男").build());
    }

}
