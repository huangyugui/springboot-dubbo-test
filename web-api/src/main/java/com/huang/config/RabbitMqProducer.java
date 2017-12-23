package com.huang.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.Queue;
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

    @Resource
    private AmqpTemplate amqpTemplate;

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
        log.info("rabbitmq配置完成......");
    }

    public void messageProducer(String message){
        log.info("消息内容：{}", message);
        Message toMsg = MessageBuilder.withBody(message.getBytes(Charset.forName("UTF-8"))).build();
        amqpTemplate.convertAndSend(exceptionExchange, exceptionPushKey, toMsg);
    }

}