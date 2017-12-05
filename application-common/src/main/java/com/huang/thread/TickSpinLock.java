package com.huang.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 * Created on 2017/11/30 14:14
 *
 * @author <a href="mailto: yugui_huang0305@163.com">黄渝贵</a>
 * @version 1.0
 */
@Slf4j
public class TickSpinLock {

    AtomicInteger tickNum  = new AtomicInteger();
    AtomicInteger serviceNum  = new AtomicInteger();
    ThreadLocal<Integer> threadNode = new ThreadLocal<>();

    public void lock(){
        int myTickNum = tickNum.getAndIncrement();
        threadNode.set(myTickNum);
        int i = 0;
        while(myTickNum != serviceNum.get()){
            try {
                TimeUnit.MILLISECONDS.sleep(100);
                log.info("Thread-{}, 自旋次数：{}", Thread.currentThread().getId(), i++);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("Thread-{}, 自旋结束，自旋次数：{}", Thread.currentThread().getId(), i++);
    }

    public void unlock(){
        int myTickNum = threadNode.get();
        while(serviceNum.compareAndSet(myTickNum, myTickNum+1)){}
    }

    public static void main(String[] args) throws InterruptedException {
        final TickSpinLock lock = new TickSpinLock();
        lock.lock();

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    lock.lock();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getId() + " acquired the lock!");
                    lock.unlock();
                }
            }).start();
        }
        Thread.sleep(300);
        System.out.println("main thread unlock!");
        lock.unlock();
    }
}
