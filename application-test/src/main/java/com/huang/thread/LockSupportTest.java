package com.huang.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * Description: LockSupport可以看做是一个二元信号量
 * Created on 2017/12/11 19:21
 *
 * @author <a href="mailto: yugui_huang0305@163.com">黄渝贵</a>
 * @version 1.0
 */
@Slf4j
public class LockSupportTest {

    private static void  test1(){
        LockSupport.park();
        System.out.println("block");
    }

    private static void  test2(){
        Thread thread = Thread.currentThread();
        LockSupport.unpark(thread);
        LockSupport.park();
        System.out.println("block");
    }

    private static void test3(){
        new Thread(() ->{
            System.out.println("t1");
            LockSupport.park();
            System.out.println("t12");
        }).start();

        new Thread(() ->{
            System.out.println("t2");
            LockSupport.unpark(Thread.currentThread());
            System.out.println("t22");
            LockSupport.park();
            System.out.println("t23");
            LockSupport.park();
            System.out.println("t24");
        }).start();

        new Thread(() ->{
            System.out.println("t3");
            LockSupport.park();
            System.out.println("t32");

        }).start();
    }

    private static void test4(){
        Thread thread1 = new Thread(() ->{
            System.out.println("t1");
            LockSupport.park();
            System.out.println("t12");
        });
        thread1.start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LockSupport.unpark(thread1);
    }

    private static void test5(){
        Thread thread1 = new Thread(() ->{
            System.out.println("t1");
            //最多不超过2秒钟，如果前面
            LockSupport.parkNanos(3000000000l);
            System.out.println("parkNanos");
            System.out.println("t12");
        });
        thread1.start();
    }

    private static void test6(){
        Thread thread1 = new Thread(() ->{
            System.out.println("t1");
            LockSupport.parkUntil(System.currentTimeMillis() + 2000);
            System.out.println("t12");
        });
        thread1.start();
    }

    private void test7(){
        Thread thread1 = new Thread(() ->{
            System.out.println("t1");
            LockSupport.parkUntil(this, System.currentTimeMillis() + 2000);
            System.out.println("t12");
        });
        thread1.start();
    }

    /**
     * Discription: park被打断，并不抛异常
     * Created on: 2017/12/27 14:39
     * @param:
     * @return:
     * @author: <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
     */
    private void test8(){
        Thread thread1 = new Thread(() ->{
            System.out.println("t1");
            LockSupport.parkUntil(this, System.currentTimeMillis() + 2000);
            log.info("LockSupport.parkUntil 2s");
            System.out.println("t12");
        });
        thread1.start();
        try {
            TimeUnit.SECONDS.sleep(1);
            thread1.interrupt();
            log.info("thread1.interrupt();");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args){
        LockSupportTest t = new LockSupportTest();
        t.test8();
    }
}
