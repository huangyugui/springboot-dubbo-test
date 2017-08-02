package com.huang.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.huang.dto.OrderDto;
import com.huang.entry.Order;
import com.huang.service.OrderService;
import com.huang.serviceApi.OrderServiceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * Created by Administrator on 2017/7/31.
 */
@Service
@Slf4j
public class OrderServiceApiImpl implements OrderServiceApi {

    @Autowired
    private OrderService orderService;

    @Override
    public String saveOrder(OrderDto orderDto) {
        Order order = new Order();
        BeanUtils.copyProperties(orderDto, order);
        return orderService.saveOrder(order);
    }

    @Override
    public OrderDto queryByOrderNo(String orderNo) {
        Order order = orderService.queryByOrderNo(orderNo);
        if(order == null){
            return null;
        }
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(order, orderDto);
        return orderDto;
    }

    @Override
    public String delOrder(OrderDto orderSample) {
        Order order = new Order();
        BeanUtils.copyProperties(orderSample, order);
        return orderService.delOrder(order);
    }

    @Override
    public String updateOrder(OrderDto orderSample) {
        Order order = new Order();
        BeanUtils.copyProperties(orderSample, order);
        return orderService.updateOrder(order);
    }
}
