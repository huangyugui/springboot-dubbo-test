package com.huang.entry;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/31.
 */
@Getter
@Setter
@ToString
public class Order {

    private Long id;
    private String orderNo;
    private String name;
    private int age;
    private String sex;
}
