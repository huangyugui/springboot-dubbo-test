package com.huang.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
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
@Component
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
    }


//    @RabbitListener(queues = {"exception_queue"})
    public void process(Map<String, String> map, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) {
        try {
            channel.basicQos(2, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        executor.execute(() -> {
            log.info("msg: {}", JSON.toJSONString(map));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            int i = 1 / 0;
            log.info("手动确认.......");
            try {
                channel.basicAck(deliveryTag, false);//deliveryTag为消息序列号,手动确认消息,队列中持久化的消息会被删除
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }


}
