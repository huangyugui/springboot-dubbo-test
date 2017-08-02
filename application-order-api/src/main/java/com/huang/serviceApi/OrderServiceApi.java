package com.huang.serviceApi;

import com.huang.dto.OrderDto;

/**
 * Created by Administrator on 2017/7/31.
 */
public interface OrderServiceApi {

    String saveOrder(OrderDto order);

    OrderDto queryByOrderNo(String orderNo);

    String delOrder(OrderDto orderSample);

    String updateOrder(OrderDto order);

}
