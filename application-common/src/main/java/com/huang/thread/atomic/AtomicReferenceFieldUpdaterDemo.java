package com.huang.thread.atomic;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * Description: 为了保证查询更新是一个原子性的操作
 * Created on 2018/2/6 17:26
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class AtomicReferenceFieldUpdaterDemo {

    @AllArgsConstructor
    @Getter
    @Setter
    static class Demo1{
        private String name;
        private String age;
    }

    public static AtomicReferenceFieldUpdater getUpdater(){
        return AtomicReferenceFieldUpdater.newUpdater(AtomicReferenceFieldUpdaterDemo.class, Demo1.class, "demo1");

    }

    private volatile Demo1 demo1 = new Demo1("name1", "age1");

    private static void test1(){
        AtomicReferenceFieldUpdaterDemo updaterDemo = new AtomicReferenceFieldUpdaterDemo();
        Demo1 demo2 = new Demo1("name2", "age2");
        getUpdater().compareAndSet(updaterDemo, updaterDemo.demo1, demo2);
        System.out.println(JSON.toJSONString(demo2));
    }

    public static void main(String[] args){
        test1();
    }


}


