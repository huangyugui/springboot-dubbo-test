package com.huang.service.impl;

import com.huang.dao.OrderMapper;
import com.huang.dto.OrderDto;
import com.huang.entry.Order;
import com.huang.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2017/7/31.
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public String saveOrder(Order order) {
        Long id = orderMapper.insert(order);
        return id.toString();
    }

    @Override
    public Order queryByOrderNo(String orderNo) {
        Order order = orderMapper.queryByOrderNo(orderNo);
        return  order;
    }

    @Override
    public String delOrder(Order orderSample) {
        orderMapper.delete(orderSample);
        return "0";
    }

    @Override
    public String updateOrder(Order order) {
        orderMapper.updateByPrimaryKey(order);
        return "0";
    }
}
