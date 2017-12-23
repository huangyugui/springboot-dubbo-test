package com.huang.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huang.config.RabbitMqProducer;
import com.huang.dto.OrderDto;
import com.huang.manager.OrderManager;
import com.huang.manager.RabbitProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

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
    private RabbitMqProducer rabbitMqProducer;

    @Autowired
    private RabbitProducer rabbitProducer;

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
            rabbitMqProducer.messageProducer(JSON.toJSONString(map));
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


    @GetMapping("/exception2")
    public String exception2(){
        for(int i = 0; i < 10; i++){
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            Map<String, String> map = new HashMap();
            map.put("key", "key" + i);
            map.put("name", "lisi" + i);
            log.info("correlationData：{}, msg: {}", correlationData.getId(), JSON.toJSONString(map));
            //设置发送确认
            rabbitTemplate.setConfirmCallback(rabbitProducer);
//            rabbitTemplate.convertAndSend(exceptionExchange, exceptionPushKey, JSON.toJSONString(map), correlationData);
            rabbitTemplate.convertAndSend(exceptionExchange, exceptionPushKey, JSON.toJSONString(map));//如果不设置correlationData，则接收的correlationData也为null
        }

        return "success";
    }

}
