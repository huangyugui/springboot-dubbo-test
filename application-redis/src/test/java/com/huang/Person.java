package com.huang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description:
 * Created on 2017/9/27 14:50
 *
 * @author <a href="mailto: yugui_huang0305@163.com">黄渝贵</a>
 * @version 1.0
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable{

    private String id;
    private String name;
}
