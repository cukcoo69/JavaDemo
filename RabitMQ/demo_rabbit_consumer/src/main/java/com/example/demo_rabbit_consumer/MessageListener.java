package com.example.demo_rabbit_consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    @RabbitListener(queues =  MQConfig.QUEUE)
    public void listener(Message message) {
        System.out.println(message);
        System.out.println("checking");
    }

}
