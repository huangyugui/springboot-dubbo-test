package com.huang.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Description:
 * Created on 2017/10/18 11:22
 *
 * @author <a href="mailto: yugui_huang0305@163.com">黄渝贵</a>
 * @version 1.0
 *
 */
@Service
@Slf4j
public class RabbitMqProducer {

    @Value("${rabbitmq.exchange.exception}")
    private String exceptionExchange;

    @Value("${rabbitmq.queue.exception}")
    private String exceptionQuque;

    @Value("${rabbitmq.pushkey.exception}")
    private String exceptionPushKey;

    @Value("${rabbitmq.queue.exception1}")
    private String exceptionQuque1;

    @Value("${rabbitmq.pushkey.exception1}")
    private String exceptionPushKey1;

    @Value("${rabbitmq.queue.deadLetter}")
    private String deadLetterQueue;

    @Resource
    private RabbitTemplate amqpTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @PostConstruct
    public void initRabbitMQInfo(){
        log.info(exceptionExchange + ", " + exceptionQuque + "," + exceptionPushKey);
        DirectExchange directEx = new DirectExchange(exceptionExchange);
        Queue queue = new Queue(exceptionQuque);

        amqpAdmin.declareExchange(directEx);
        amqpAdmin.declareQueue(queue);

        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(directEx).with(exceptionPushKey));


        Queue queue1 = new Queue(exceptionQuque1);

        amqpAdmin.declareExchange(directEx);
        amqpAdmin.declareQueue(queue1);

        amqpAdmin.declareBinding(BindingBuilder.bind(queue1).to(directEx).with(exceptionPushKey1));


        //死信队列
        Queue deadQueue = QueueBuilder.durable("dead_letter_queue")
                .withArgument("x-dead-letter-exchange", exceptionExchange)
                .withArgument("x-dead-letter-routing-key", exceptionPushKey)
                .withArgument("x-message-ttl", 10)
                .build();
        amqpAdmin.declareQueue(deadQueue);

        log.info("rabbitmq配置完成......");
    }

}
