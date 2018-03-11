package com.huang.thread.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Description:
 * Created on 2018/2/6 17:56
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class AtomicIntegerArrayDemo {

    private static AtomicIntegerArray arr = new AtomicIntegerArray(new int[]{1, 2, 3, 4, 5});

    public static void main(String[] args){
        arr.compareAndSet(3, 5, 10);
        System.out.println(arr.get(3));
        arr.compareAndSet(3, 4, 10);
        System.out.println(arr.get(3));
    }
}
