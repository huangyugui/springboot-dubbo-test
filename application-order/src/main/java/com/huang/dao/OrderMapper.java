package com.huang.dao;

import com.huang.entry.Order;

/**
 * Created by Administrator on 2017/7/31.
 */
public interface OrderMapper {

    long insert(Order order);

    Order queryByOrderNo(String orderNo);

    int updateByPrimaryKey(Order order);

    int delete(Order orderSample);
}
