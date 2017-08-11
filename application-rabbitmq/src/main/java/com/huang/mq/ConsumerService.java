package com.huang.mq;

import com.alibaba.fastjson.JSON;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public void process(Map<String, String> map) {
        Flowable.fromCallable(() -> {
            return "Done";
        }) .subscribeOn(Schedulers.from(executor))
                .observeOn(Schedulers.from(executor))
                .subscribe(s -> {
                    try{
                        log.info("msg: {}", JSON.toJSONString(map));
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        int i = 1 / 0;
                    }catch (Exception e){
                        log.error("sign-queue队列消费失败，消息是：{}",JSON.toJSONString(map));
                        rabbitTemplate.convertAndSend("exception_exchange", "exception_pushkey", map);
                        throw new RuntimeException(e);
                    }

                });
//        ExecutorService executor = Executors.newFixedThreadPool(5);
//        executor.execute(() -> {
//            log.info("msg: {}", JSON.toJSONString(map));
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            int i = 1 / 0;
//        });

//        log.info("msg: {}", JSON.toJSONString(map));
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        int i = 1 / 0;

    }


}
