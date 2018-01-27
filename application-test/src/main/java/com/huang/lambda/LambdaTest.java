package com.huang.lambda;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



/**
 * Description:
 * Created on 2017/12/14 13:51
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
@Slf4j
public class LambdaTest {

    public static List<Person> list = new ArrayList<>();
    static{
        list.add(new Person("aaa", 20));
        list.add(new Person("bbb", 21));
        list.add(new Person("ccc", 22));
        list.add(new Person("ddd", 23));
    }

    private static void test1(){
        List t = list.stream().peek(item -> {
           Map<String, String> map = new HashMap<>();
           map.put("name", item.getName());
//           return map;
        }).collect(Collectors.toList());
        log.info("{}", JSON.toJSONString(t));
    }

    public static void main(String[] args){
        test1();
    }
}

@Getter
@Setter
@AllArgsConstructor
class Person{
    private String name;
    private int age;

}