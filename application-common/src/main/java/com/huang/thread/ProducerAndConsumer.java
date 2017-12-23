package com.huang.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description:
 * Created on 2017/12/20 11:21
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
@Slf4j
public class ProducerAndConsumer {

    public static void test1(){
        Operator operator = new Operator();

        for(int i = 0; i < 2; i++){
            new Thread(() ->{
                operator.put();
            }).start();
        }

        for (int i = 0; i < 2; i++){
            new Thread(() -> {
                operator.get();
            }).start();
        }
    }

    public static void test2(){

        Operator operator = new Operator();

        for(int i = 0; i < 10; i++){
            new Thread(() ->{
                operator.put2();
            }).start();
        }

        for (int i = 0; i <10; i++){
            new Thread(() -> {
                operator.get2();
            }).start();
        }
    }

    public static void main(String[] args){
        test1();

    }

}

@Slf4j
class Operator{

    private List<String> list = new ArrayList<>();

    private int capacity = 10;

    private int num = 0;

    ReentrantLock lock = new ReentrantLock();

    Condition full = lock.newCondition();

    Condition empty = lock.newCondition();

    public void put(){
        while(true){
            try{
                TimeUnit.MILLISECONDS.sleep(200);
                while(list.size() == capacity){
                    log.info("装不下了。。。");
                    full.await();
                }
                lock.lock();
                String str = "馒头" + ++num;
                list.add(str);
                log.info("放入: {}", str);
//                Thread.yield();
//                if(list.size() == 1){
//                    log.info("容器里面最起码有一个馒头了，来取吧。。。");
//                    empty.signal();
//                }

                empty.signal();

//                empty.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public void get(){
        while (true){
            try{
                TimeUnit.MILLISECONDS.sleep(200);
                lock.lock();
                while(list.size() == 0){
                    log.info("没馒头了。。。");
                    empty.await();
                }
                String str = list.remove(0);
                log.info("取出: {}", str);
//                Thread.yield();
//                if(list.size() + 1 == capacity){
//                    log.info("容器里面可以继续放馒头了。。。");
//                    full.signal();
//                }

                full.signal();

//                full.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    BlockingQueue<String> queue = new LinkedBlockingDeque<String>(10);
    AtomicInteger aNum = new AtomicInteger(0);

    public void put2(){
        while(true){
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String str = "馒头" + aNum.addAndGet(1);
            try {
                queue.put(str);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("queue的大小：{}，放入: {}",queue.size(), str);
//            Thread.yield();
        }
    }

    public void get2(){
        while(true){
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String str = null;
            try {
                str = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("取出: {}", str);
//            Thread.yield();
        }
    }


}
