package com.huang.queue;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Created on 2018/1/4 10:17
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
@Slf4j
@Getter
public class ConcurrentLinkedQueueTest {

    private static ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();

    private static void test1(){
        new Thread(() -> {
            for(int i = 0; i < 100; i++){
                queue.add("aaaa" + i);
            }
        }).start();
//        new Thread(() -> {
//            queue.offer("bbbb");
//        }).start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while(queue.peek() != null){
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(queue.poll());
                System.out.println(queue.remove());
                System.out.println(queue.peek());
            }
        }).start();
    }

    public static void main(String[] args){
        test1();
    }

}
