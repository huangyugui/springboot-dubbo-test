package com.huang.controller;

import com.huang.dto.OrderDto;
import com.huang.manager.OrderManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

/**
 * Created by Administrator on 2017/7/31.
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderManager orderManager;

    @RequestMapping("/{orderNo}")
    public Callable<OrderDto> queryByOrderNo(@PathVariable("orderNo") String orderNo){
        return () -> {
            return orderManager.queryByOrderNo(orderNo);
        };
    }

    @GetMapping("/query/{orderNo}")
    public OrderDto queryByOrderNo1(@PathVariable("orderNo") String orderNo){
        OrderDto orderDto = orderManager.queryByOrderNo(orderNo);
        return orderDto;
    }

}
