package com.huang.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Created on 2017/12/13 10:46
 *
 * @author <a href="mailto: yugui_huang0305@163.com">黄渝贵</a>
 * @version 1.0
 */
@Slf4j
public class InterruptedTest {

    private static final int[] array = new int[30000];

    static {
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(i + 1);
        }
    }

    private static int sort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] < array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        return array[0];
    }

    //当线程中断的时候，并不会真正的中断，只是在线程中断标志位上设置了一个值，并不会立马中断该线程
    //该线程需要自己去控制中断
    private static void test1() {
        Thread t1 = new Thread(() -> {
            sort(array);
            log.info("当前中断状态为：{}", Thread.currentThread().isInterrupted());
            if (Thread.currentThread().isInterrupted()) {
//                Thread.currentThread().interrupt();
                log.info("线程已经被打断");
                return;
            }
        });
        t1.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("准备打断t1");
        t1.interrupt();
    }

    //调用interrupted()方法会重置中断状态，线程被中断之后，如果连续两次调用，第一次返回true，第二次返回false
    private static void test2(){
        Thread t1 = new Thread(() -> {
            sort(array);
            log.info("当前中断状态为：{}", Thread.currentThread().isInterrupted());
            log.info("第一次中断：{}", Thread.currentThread().interrupted());
            log.info("第二次中断：{}", Thread.currentThread().interrupted());
        });
        t1.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("准备打断t1");
        t1.interrupt();
    }

    //调用sleep时，如果被interrupt，那么线程立马从休眠中醒过来，同时重置中断状态
    private static void test3(){
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("当前中断状态为：{}", Thread.currentThread().isInterrupted());
        });
        t1.start();
        log.info("t1 start");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("准备打断t1");
        t1.interrupt();
    }


    public static void main(String[] args) {
        test3();
    }
}
