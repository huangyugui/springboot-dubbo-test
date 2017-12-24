package com.huang.manager;

import com.alibaba.dubbo.config.annotation.Reference;
import com.huang.dto.OrderDto;
import com.huang.serviceApi.OrderServiceApi;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/7/31.
 */
@Service
public class OrderManager {

    @Reference(check = false, timeout = 5000)
    private OrderServiceApi orderServiceApi;

    public OrderDto queryByOrderNo(String orderNo){
        OrderDto orderDto = orderServiceApi.queryByOrderNo(orderNo);
        return orderDto;
    }

    public OrderDto query(String orderNo){
        OrderDto orderDto = new OrderDto();

//        orderDto.setAge(11);
//        orderDto.setId(16l);
//        orderDto.setName("账单");
        return orderDto;
    }

}
