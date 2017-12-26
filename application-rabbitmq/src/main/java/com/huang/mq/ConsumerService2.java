package com.huang.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:手动确认消息
 * Created on 2017/8/11 10:44
 *
 * @author <a href="mailto: yugui_huang0305@163.com">黄渝贵</a>
 * @version 1.0
 *
 */
@Component
@Slf4j
public class ConsumerService2 implements ChannelAwareMessageListener{

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    private static int count = 1;

    @Override
    @RabbitListener(queues = {"${rabbitmq.queue.exception1}"})
//    @RabbitListener(queues = {"dead_letter_queue"})
    public void onMessage(Message message, Channel channel) throws Exception {
        log.info("{}", JSON.toJSONString(message.getMessageProperties().getHeaders().get("x-death")));
        log.info("接收的消息：{}，通知次数：{}", new String(message.getBody()), count++);
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);//手动确认消息，队列中持久化消息会被删除
        channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);//手动否认一条消息,消息会被无限次重新接收,直到确认消息
//        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);//手动否认消息,消息会被无限次重新接收,直到确认消息
//        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);//如果将requeue设置为false，将不会重新回到队列，会被丢弃或者扔到死信队列里面
        //throw new RuntimeException("测试异常是否重新接收");//抛出运行异常时,消息不会被重新接收
    }

}
