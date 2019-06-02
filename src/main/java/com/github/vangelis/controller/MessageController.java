package com.github.vangelis.controller;

import com.github.vangelis.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发送消息的Controller
 *
 * @author Vangelis
 * @date 2019-06-02 11:26
 */
@RestController
@RequestMapping("message")
@AllArgsConstructor
@Slf4j
public class MessageController {

    private final RabbitTemplate rabbitTemplate;

    @GetMapping
    public String sendMessage(String name) {
        rabbitTemplate.convertAndSend("direct_exchange","message_direct",
                User.builder().introduction("我是一个小毛驴").name(name).sex("男").build());
        return "send direct to" + name;
    }

}
