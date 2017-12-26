package com.huang.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;

/**
 * Description:
 * Created on 2017/12/25 17:36
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
@Component
@Slf4j
@ConditionalOnBean(value={RabbitConfirmCallback.class, RabbitReturnCallback.class})
public class RabbitMqSender {

    @Autowired
    RabbitConfirmCallback confirmCallback;

    @Autowired
    RabbitReturnCallback returnCallback;

    @Value("${rabbitmq.exchange.exception}")
    private String exceptionExchange;

    @Value("${rabbitmq.pushkey.exception}")
    private String exceptionPushKey;

    @Value("${rabbitmq.pushkey.exception1}")
    private String exceptionPushKey1;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
    }

    public void messageProducer(String message){
        log.info("消息内容：{}", message);
        Message toMsg = MessageBuilder.withBody(message.getBytes(Charset.forName("UTF-8"))).build();
        rabbitTemplate.convertAndSend(exceptionExchange, exceptionPushKey, toMsg);
    }

    public void messageProducer(String message, CorrelationData correlationData){
        log.info("消息内容：{}", message);
        Message toMsg = MessageBuilder.withBody(message.getBytes(Charset.forName("UTF-8"))).build();
        rabbitTemplate.convertAndSend(exceptionExchange, exceptionPushKey, toMsg, correlationData);
    }

    public void messageProducer(String exchange, String routingKey, boolean mandatory, String message, CorrelationData correlationData){
        log.info("消息内容：{}", message);
        Message toMsg = MessageBuilder.withBody(message.getBytes(Charset.forName("UTF-8"))).build();
        rabbitTemplate.setMandatory(mandatory);
        rabbitTemplate.convertAndSend(exchange, routingKey, toMsg, correlationData);
    }

    public void messageProducer(String exchange, String routingKey, boolean mandatory, String timeout, String message, CorrelationData correlationData){
        log.info("消息内容：{}", message);
        Message toMsg = MessageBuilder.withBody(message.getBytes(Charset.forName("UTF-8")))
                .setExpiration(timeout)
                .build();
        rabbitTemplate.setMandatory(mandatory);
        rabbitTemplate.convertAndSend(exchange, routingKey, toMsg, correlationData);
    }

    public Object messageProducer(String exchange, String routingKey, String message){
        log.info("消息内容：{}", message);
        Message toMsg = MessageBuilder.withBody(message.getBytes(Charset.forName("UTF-8")))
                .build();
        return rabbitTemplate.convertSendAndReceive(exchange, routingKey, toMsg);
    }

}
