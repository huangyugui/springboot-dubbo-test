package com.huang.controller;

import com.alibaba.fastjson.JSON;
import com.huang.config.RabbitConfirmCallback;
import com.huang.config.RabbitMqSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 *
 * rabbitmq可靠发送的自动重试机制:https://www.jianshu.com/p/6579e48d18ae
 *
 * 用30个线程，无间隔的向rabbitmq发送数据，但是当运行一段时间后发现，会出现一些connection closed错误，
 * rabbitTemplate虽然进行了自动重连，但是在重连的过程中，丢失了一部分数据。当时发送了300万条数据，丢失在2000条左右。
     这种丢失率，对于一些对一致性要求很高的应用(比如扣款，转账)来说，是不可接受的。
     在google了很久之后，在stackoverflow上找到rabbitTemplate作者对于这种问题的解决方案，\
    他给的方案很简单，单纯的增加connection数：
     connectionFactory.setChannelCacheSize(100);
 *
 *
 * 当我们的网络在发生抖动时，这种方式还是不是安全的？
 * 换句话说，如果我强制切断客户端和rabbitmq服务端的连接，数据还会丢失吗？仍然存在丢失数据的问题。
 * 可以采用发送之前本地先缓存，开启发送端ack功能，然后定时的去查询本地消息没有被ack的，然后开启重发
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
        for(int i = 0; i < 100; i++){
            Map<String, String> map = new HashMap();
            map.put("key", "key" + i);
            map.put("name", "lisi" + i);
            log.info("msg: {}" + JSON.toJSONString(map));
            rabbitTemplate.convertAndSend(exceptionExchange, exceptionPushKey, JSON.toJSONString(map));
        }

        for(int i = 100; i < 200; i++){
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

    /** 
     * Discription: 如果 rabbitTemplate.setMandatory(true);  如果routingKey不存在，且设置了rabbitTemplate.setReturnCallback(returnCallback);
     *      那么该消息就会被返回回来
     * Created on: 2018/1/10 11:29
     * @param:  
     * @return: 
     * @author: <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
     */
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

    /**
     * Discription: 优先级队列
     * Created on: 2018/1/10 11:29
     * @param:
     * @return:
     * @author: <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
     */
    @GetMapping("/exception6")
    public String exception6(){
        for(int i = 0; i < 60; i++){
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            Map<String, String> map = new HashMap();
            map.put("key", "key" + i);
            map.put("name", "lisi" + i);
            int priority = new Random().nextInt(20);
            map.put("priority", priority + "");
            System.out.println(JSON.toJSONString(map));
            Message toMsg = MessageBuilder.withBody(JSON.toJSONString(map).getBytes(Charset.forName("UTF-8")))
                    .setPriority(priority)
                    .build();
            rabbitTemplate.convertAndSend(exceptionExchange, "priority_queue", toMsg);
        }
        return "success";
    }


}
