package com.huang.service;

import com.huang.dto.OrderDto;
import com.huang.entry.Order;
import com.huang.serviceApi.OrderServiceApi;

/**
 * Created by Administrator on 2017/7/31.
 */
public interface OrderService {

    String saveOrder(Order order);

    Order queryByOrderNo(String orderNo);

    String delOrder(Order orderSample);

    String updateOrder(Order order);
}
