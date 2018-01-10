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

    /** 
     * Discription:
     *  队列的排他性：QueueBuilder.durable("priority_queue").exclusive()
     *      1、只对首次声明它的连接可见：
     *          首先强调声明，另外一个连接无法声明一个同样排他性的队列，
     *          其次强调连接，只区别连接(Connection)，而不是通道（channel)，从同一个连接创建的不同通道可以访问某一个排他性的队列，
     *          如果其他连接试图访问该排他性队列，会得到资源锁定的错误：ESOURCE_LOCKED - cannot obtain exclusive access to locked queue 'UserLogin2'
     *      2、会在其连接断开的时候自动删除：
     *          不管是否声明成持久性的，只要调用连接的close方法或者客户端程序退出，RabbitMq都会删除这个队列
     *
     * Created on: 2018/1/10 11:40
     * @param:  
     * @return: 
     * @author: <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
     */
    @PostConstruct
    public void initRabbitMQInfo(){
        //dead_letter_queue的 死信队列指向 exception_exchange:exception_pushkey1
        Queue deadQueue = QueueBuilder.durable("dead_letter_queue")
                .withArgument("x-dead-letter-exchange", exceptionExchange)
                .withArgument("x-dead-letter-routing-key", exceptionPushKey1)
                .withArgument("x-message-ttl", 10000)
                .build();
        amqpAdmin.declareQueue(deadQueue);
        DirectExchange dle = new DirectExchange("dle");
        amqpAdmin.declareExchange(dle);
        amqpAdmin.declareBinding(BindingBuilder.bind(deadQueue).to(dle).with("dlk"));

        //exception_queue 的死信队列指向  dle:dlk
        DirectExchange directEx = new DirectExchange(exceptionExchange);
        Queue queue = QueueBuilder.durable(exceptionQuque)
                .withArgument("x-dead-letter-exchange", "dle")
                .withArgument("x-dead-letter-routing-key", "dlk")
                .build();
        amqpAdmin.declareExchange(directEx);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(directEx).with(exceptionPushKey));

        //创建  exception_queue1队列
        Queue queue1 = new Queue(exceptionQuque1);
        amqpAdmin.declareQueue(queue1);
        amqpAdmin.declareBinding(BindingBuilder.bind(queue1).to(directEx).with(exceptionPushKey1));

        //创建优先级队列
        Queue priorityQueue = QueueBuilder.durable("priority_queue").withArgument("x-max-priority", 20).build();
        amqpAdmin.declareQueue(priorityQueue);
        amqpAdmin.declareBinding(BindingBuilder.bind(priorityQueue).to(directEx).with("priority_queue"));

//        amqpAdmin.declareQueue(new Queue("repay-queue"));
//        amqpTemplate.setReplyAddress("repay-queue");

        log.info("rabbitmq配置完成......");
    }

}
