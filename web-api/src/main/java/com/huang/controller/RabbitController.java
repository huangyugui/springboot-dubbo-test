package com.huang.controller;

import com.alibaba.fastjson.JSON;
import com.huang.config.RabbitConfirmCallback;
import com.huang.config.RabbitMqSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2017/7/31.
 */
@RestController
@Slf4j
@RequestMapping("/rabbit")
public class RabbitController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMqSender sender;

    @Value("${rabbitmq.exchange.exception}")
    private String exceptionExchange;

    @Value("${rabbitmq.pushkey.exception}")
    private String exceptionPushKey;

    @Value("${rabbitmq.pushkey.exception1}")
    private String exceptionPushKey1;

    @GetMapping("/exception")
    public String exception(){
        for(int i = 0; i < 10; i++){
            Map<String, String> map = new HashMap();
            map.put("key", "key" + i);
            map.put("name", "lisi" + i);
            log.info("msg: {}", JSON.toJSONString(map));
            sender.messageProducer(JSON.toJSONString(map));
        }
        return "success";
    }

    @GetMapping("/exception1")
    public String exception1(){
        for(int i = 0; i < 10; i++){
            Map<String, String> map = new HashMap();
            map.put("key", "key" + i);
            map.put("name", "lisi" + i);
            log.info("msg: {}" + JSON.toJSONString(map));
            rabbitTemplate.convertAndSend(exceptionExchange, exceptionPushKey, JSON.toJSONString(map));
        }

        for(int i = 10; i < 20; i++){
            Map<String, String> map = new HashMap();
            map.put("key", "key" + i);
            map.put("name", "lisi" + i);
            log.info("msg: {}" + JSON.toJSONString(map));
            rabbitTemplate.convertAndSend(exceptionExchange, exceptionPushKey1, JSON.toJSONString(map));
        }
        return "success";
    }

    /**
     * Discription: 设置发送手动确认,需要配置 publisher-confirms: true
     * Created on: 2017/12/25 17:01
     * @param:
     * @return:
     * @author: <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
     */
    @GetMapping("/exception2")
    public String exception2(){
        for(int i = 0; i < 10; i++){
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            Map<String, String> map = new HashMap();
            map.put("key", "key" + i);
            map.put("name", "lisi" + i);
            log.info("correlationData：{}, msg: {}", correlationData.getId(), JSON.toJSONString(map));
            sender.messageProducer(JSON.toJSONString(map), correlationData);
            //设置发送确认
//            rabbitTemplate.setConfirmCallback();
//            rabbitTemplate.convertAndSend(exceptionExchange, exceptionPushKey, JSON.toJSONString(map), correlationData);
//            rabbitTemplate.convertAndSend(exceptionExchange, exceptionPushKey, JSON.toJSONString(map));//如果不设置correlationData，则接收的correlationData也为null
        }
        return "success";
    }


    /**
     * Discription: 设置Mandatory为true，如果exchange根据自身类型和消息routeKey无法找到一个符合条件的queue，
     *      那么会调用basic.return方法将消息返回给生产者（Basic.Return + Content-Header + Content-Body）；
     *      当mandatory设置为false时，出现上述情形broker会直接将消息扔掉。
     * Created on: 2017/12/25 17:01
     * @param:
     * @return:
     * @author: <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
     */
    @GetMapping("/exception3")
    public String exception3(){
        for(int i = 0; i < 10; i++){
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            Map<String, String> map = new HashMap();
            map.put("key", "key" + i);
            map.put("name", "lisi" + i);
//            log.info("correlationData：{}, msg: {}", correlationData.getId(), JSON.toJSONString(map));
            //设置发送确认
            sender.messageProducer(exceptionExchange, "aaaaa", true, JSON.toJSONString(map), correlationData);
        }
        return "success";
    }

    /** 
     * Discription: 设置消息的有效期，有效期到了之后，如果有死信队列，则会到死信队列里面，如果没有则会被抛弃
     * [{"reason":"expired","count":1,"exchange":"dle","routing-keys":["dlk"],"time":1514204838000,"queue":"dead_letter_queue"},
     * {"reason":"expired","original-expiration":"10000","count":1,"exchange":"exception_exchange","time":1514204828000,"routing-keys":["exception_pushkey"],"queue":"exception_queue"}]
     *
          queue - the name of the queue the message was in before it was dead-lettered,在成为死信时所在的队列
         reason - see below,
         time - the date and time the message was dead lettered as a 64-bit AMQP format timestamp, 成为死信时的时间
         exchange - the exchange the message was published to (note that this will be a dead letter exchange if the message is dead lettered multiple times), and
                这个消息被发布到了哪个exchange里面，如果成为死信并到了死信队列1，并且超时，到了死信队列1指定的queue里面，则他的值为死信队列1的exchange
         routing-keys - the routing keys (including CC keys but excluding BCC ones) the message was published with.
         original-expiration (if the message was dead-letterered due to per-message TTL) - the originalexpiration property of the message. The expiration property is removed from the message on dead-lettering in order to prevent it from expiring again in any queues it is routed to.
                超时时间
         The reason is a name describing why the message was dead-lettered and is one of the following:

         rejected - the message was rejected with requeue=false,
         expired - the TTL of the message expired; or
         maxlen - the maximum allowed queue length was exceeded.
     * Created on: 2017/12/25 19:40 
     * @param:  
     * @return: 
     * @author: <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
     */
    @GetMapping("/exception4")
    public String exception4(){
        for(int i = 0; i < 10; i++){
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            Map<String, String> map = new HashMap();
            map.put("key", "key" + i);
            map.put("name", "lisi" + i);
//            log.info("correlationData：{}, msg: {}", correlationData.getId(), JSON.toJSONString(map));
            //设置发送确认
            sender.messageProducer(exceptionExchange, exceptionPushKey, true, "10000", JSON.toJSONString(map), correlationData);
        }
        return "success";
    }

    @GetMapping("/exception5")
    public String exception5(){
        for(int i = 0; i < 10; i++){
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            Map<String, String> map = new HashMap();
            map.put("key", "key" + i);
            map.put("name", "lisi" + i);
            //设置发送确认
            Object obj = sender.messageProducer(exceptionExchange, "aaa", JSON.toJSONString(map));
            log.info("{}", JSON.toJSONString(obj));
        }
        return "success";
    }


}
