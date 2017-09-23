package com.huang.config;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * Description:
 * Created on 2017/8/11 14:52
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 *          Copyright (c) 2017 国美金控-美借
 */
@Configurable
public class RabbitConfig {

    @Value("rabbitmq.exchange.exception")
    private String exceptionExchange;

    @Value("rabbitmq.queue.exception")
    private String exceptionQuque;

    @Value("rabbitmq.queue.exception")
    private String exceptionPushKey;

    @Bean
    public Queue exceptionQueue(){
        return new Queue(exceptionQuque);
    }

    @Bean
    public FanoutExchange exceptionExchange(){
        return new FanoutExchange(exceptionExchange);
    }

    @Bean
    public Binding bindingExchangePushMessage(DirectExchange exceptionExchange, Queue exceptionQueue){
        return BindingBuilder.bind(exceptionQueue).to(exceptionExchange).with(exceptionPushKey);
    }

}
