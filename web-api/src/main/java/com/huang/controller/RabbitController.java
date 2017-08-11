package com.huang.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huang.dto.OrderDto;
import com.huang.manager.OrderManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Administrator on 2017/7/31.
 */
@RestController
@Slf4j
@RequestMapping("/rabbit")
public class RabbitController {

    @Autowired
    private AmqpTemplate rabbitTemplate;

//    @Value("rabbitmq.exchange.exception")
//    private String exceptionExchange;
//
//    @Value("rabbitmq.pushkey.exception")
//    private String exceptionPushKey;

    @GetMapping("/exception")
    public String exception(){
        for(int i = 0; i < 10; i++){
            Map<String, String> map = new HashMap();
            map.put("key", "key" + i);
            map.put("name", "lisi" + i);
            log.info("msg: {}" + JSON.toJSONString(map));
            rabbitTemplate.convertAndSend("exception_exchange", "exception_pushkey", map);
        }
        return "success";
    }

}
