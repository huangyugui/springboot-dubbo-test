package com.huang.thread;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: CyclicBarrier是必须等所有的线程都执行完毕了，才可以继续以后的事情
 * Created on 2018/3/1 19:57
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class CyclicBarrierTest {

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger num = new AtomicInteger(1);
        CyclicBarrier cb = new CyclicBarrier(3, () -> {
            num.incrementAndGet();
        });

        //共三道关卡
        //所有的人必须同时到达关卡门口的时候，此关卡才能通过
        Person p1 = new Person(cb, num, "红红");
        Person p2 = new Person(cb, num, "兰兰");
        Person p3 = new Person(cb, num, "彤彤");
        new Thread(p1).start();
        new Thread(p2).start();
        new Thread(p3).start();
    }


}

class Person implements Runnable{

    private CyclicBarrier cb;

    private AtomicInteger num;

    private String name;

    public Person(CyclicBarrier cb, AtomicInteger num, String name){
        this.name = name;
        this.cb = cb;
        this.num = num;
    }

    @Override
    public void run() {
        while(num.get() <= 3){
            System.out.println(name + "开始闯"+num+"关，打怪ing");
            Random random = new Random();
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(num.get() == 2 && "兰兰".equals(name)){
                //处于await的线程重新执行，当前线程继续往下执行
                cb.reset();
                System.out.println("reset");
            }
            System.out.println(name + "到达了第" + num + "道关卡");

            try {
//            System.out.println(name + "=======" +cb.await());
                cb.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
