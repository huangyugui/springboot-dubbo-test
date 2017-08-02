package com.huang.dto;


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
public class OrderDto implements Serializable{

    private static final long serialVersionUID = 3131808708432303266L;

    private Long id;
    private String orderNo;
    private String name;
    private int age;
    private String sex;
}
