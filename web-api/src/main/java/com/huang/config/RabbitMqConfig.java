package com.huang.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.Charset;

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
public class RabbitMqConfig {

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
        //死信队列
        Queue deadQueue = QueueBuilder.durable("dead_letter_queue")
                .withArgument("x-dead-letter-exchange", exceptionExchange)
                .withArgument("x-dead-letter-routing-key", exceptionPushKey1)
                .withArgument("x-message-ttl", 10000)
                .build();
        amqpAdmin.declareQueue(deadQueue);

        DirectExchange dle = new DirectExchange("dle");
        amqpAdmin.declareExchange(dle);
        amqpAdmin.declareBinding(BindingBuilder.bind(deadQueue).to(dle).with("dlk"));

        DirectExchange directEx = new DirectExchange(exceptionExchange);
        Queue queue = QueueBuilder.durable(exceptionQuque)
                .withArgument("x-dead-letter-exchange", "dle")
                .withArgument("x-dead-letter-routing-key", "dlk")
                .build();

        amqpAdmin.declareExchange(directEx);
        amqpAdmin.declareQueue(queue);

        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(directEx).with(exceptionPushKey));


        Queue queue1 = new Queue(exceptionQuque1);

        amqpAdmin.declareExchange(directEx);
        amqpAdmin.declareQueue(queue1);

        amqpAdmin.declareBinding(BindingBuilder.bind(queue1).to(directEx).with(exceptionPushKey1));

//        amqpAdmin.declareQueue(new Queue("repay-queue"));
//        amqpTemplate.setReplyAddress("repay-queue");

        log.info("rabbitmq配置完成......");
    }

}
