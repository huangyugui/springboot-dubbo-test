package com.huang.queue;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Created on 2018/1/4 17:17
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
@Slf4j
public class LinkedBlockingQueueTest {

    private static LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>(10);

    private static void test1(){
        new Thread(() -> {
            log.info("wait for poll");
            try {
                String str = queue.poll(500, TimeUnit.SECONDS);
                log.info("wait for value = {}", str);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("add node");
            queue.add("aaaa");
        }).start();
    }

    public static void main(String[] args){
        test1();
    }

}
