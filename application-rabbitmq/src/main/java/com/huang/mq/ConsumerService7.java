package com.huang.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component
@Slf4j
public class ConsumerService7 {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    @RabbitListener(queues = {"exception_queue"})
    public void process(Message message){
        log.info(new String(message.getBody()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("aaaaa");
    }




}
