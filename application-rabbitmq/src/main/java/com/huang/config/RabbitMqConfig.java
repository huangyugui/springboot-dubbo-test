package com.huang.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
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
@Configuration
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

//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setAddresses("127.0.0.1:5672");
//        connectionFactory.setUsername("admin");
//        connectionFactory.setPassword("admin");
//        connectionFactory.setVirtualHost("sit");
//        connectionFactory.setPublisherConfirms(true); //必须要设置
//        return connectionFactory;
//    }
//
//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        //factory.setMessageConverter(new Jackson2JsonMessageConverter());
//        return factory;
//    }
//
//    @Bean
//    /** 因为要设置回调类，所以应是prototype类型，如果是singleton类型，则回调类为最后一次设置 */
//    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//    public RabbitTemplate rabbitTemplate() {
//        RabbitTemplate template = new RabbitTemplate(connectionFactory());
//        return template;
//    }

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

        log.info("rabbitmq配置完成......");
    }

}
