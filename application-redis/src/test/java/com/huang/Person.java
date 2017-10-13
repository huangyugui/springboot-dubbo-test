package com.huang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description:
 * Created on 2017/9/27 14:50
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 *          Copyright (c) 2017 国美金控-美借
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable{

    private String id;
    private String name;
}
