package com.huang.queue;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Description:
 * Created on 2018/1/5 11:32
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class PriorityBlockingQueueTest {

    private static PriorityBlockingQueue queue;

    static {
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };
        queue = new PriorityBlockingQueue(11, comparator);
    }

    private static void test1(){
        queue.add("aaa");
        queue.add("ccc");
        queue.add("bbb");
        queue.add("eee");
        queue.add("ddd");
        try {
            System.out.println(queue.take());
            System.out.println(queue.take());
            System.out.println(queue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        test1();
    }
}
