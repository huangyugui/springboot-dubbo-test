package com.huang.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 *          Copyright (c) 2017 国美金控-美借
 */
@Component
@Slf4j
public class ConsumerService {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    @RabbitListener(queues = {"exception_queue"})
    public void process(Map<String, String> map, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) {
//        Flowable.fromCallable(() -> {
//            return "Done";
//        }) .subscribeOn(Schedulers.from(executor))
//                .observeOn(Schedulers.from(executor))
//                .subscribe(s -> {
////                    try{
//                        log.info("msg: {}", JSON.toJSONString(map));
//                        try {
//                            Thread.sleep(500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        int i = 1 / 0;
//                        log.info("手动确认.......");
//                        channel.basicAck(deliveryTag, false);//deliveryTag为消息序列号,手动确认消息,队列中持久化的消息会被删除
//                        //channel.basicReject(deliveryTag, true);//手动否认一条消息,消息会被无限次重新接收,直到确认消息
//                        //channel.basicNack(deliveryTag, false, true);//手动否认多条小于deliveryTag序列消息,消息会被无限次重新接收,直到确认消息
//                        //throw new RuntimeException("测试异常是否重新接收");//抛出运行异常时,消息不会被重新接收
////                    }catch (Exception e){
////                        log.error("sign-queue队列消费失败，消息是：{}",JSON.toJSONString(map));
//////                        throw new RuntimeException(e);//多线程中采用自动确认，即使throw Exception，也不会使消息回到MQ队里中，可以使用手动的方式
////                    }
//
//                });

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
            int i = 1 / 0;
            log.info("手动确认.......");
            try {
                channel.basicAck(deliveryTag, false);//deliveryTag为消息序列号,手动确认消息,队列中持久化的消息会被删除
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

//        log.info("msg: {}", JSON.toJSONString(map));
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        int i = 1 / 0;
//        log.info("手动确认.......");
//        try {
//            channel.basicAck(deliveryTag, false);//deliveryTag为消息序列号,手动确认消息,队列中持久化的消息会被删除
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }


}
