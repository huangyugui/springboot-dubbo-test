package com.huang.thread;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 任务运行完毕继续干其他事情，某个线程先wait，等待其他线程全部执行完毕后，该线程才继续往下执行
 * Created on 2018/3/1 19:37
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class CountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        //发令枪
        CountDownLatch begin = new CountDownLatch(1);
        //记录仪
        CountDownLatch end = new CountDownLatch(5);

        AtomicInteger num = new AtomicInteger(0);

        for(int i = 0; i < 5; i++){
            new Thread(new Player(begin, end, num)).start();
        }

        //发令枪响了
        while(num.get() != 5){}

        begin.countDown();

        //记录仪记录成绩
        end.await();
        System.out.println("将成绩上报.....");

    }

}

class Player implements Runnable{

    private CountDownLatch begin;

    private CountDownLatch end;

    private AtomicInteger num;

    public Player(CountDownLatch begin, CountDownLatch end, AtomicInteger num){
        this.begin = begin;
        this.end = end;
        this.num = num;
    }

    @Override
    public void run() {

        try {
            //等待发令枪响
            System.out.println(Thread.currentThread().getName() + "第" + num.incrementAndGet() + "准备好了....");
            begin.await();

            System.out.println(Thread.currentThread().getName() + "开始跑，run， run， run");
            Random random = new Random();
            Thread.sleep(random.nextInt(500));
            System.out.println(Thread.currentThread().getName() + "准备撞线了");
            end.countDown();

            System.out.println(Thread.currentThread().getName() + "冲过了终点");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
