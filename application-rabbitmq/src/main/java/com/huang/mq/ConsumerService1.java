package com.huang.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:
 * Created on 2017/8/11 10:44
 *
 * @author <a href="mailto: yugui_huang0305@163.com">黄渝贵</a>
 * @version 1.0
 *
 */
//@Component
@Slf4j
public class ConsumerService1 {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    /**
     * 如果发送方传递的是Message对象，那么此处不能使用String来接收
     * @param message
     */
    @RabbitListener(queues = {"exception_queue"})
    public void process(String message){
        log.info(message);
        throw new AmqpRejectAndDontRequeueException("test-dead-letter");
    }




}
