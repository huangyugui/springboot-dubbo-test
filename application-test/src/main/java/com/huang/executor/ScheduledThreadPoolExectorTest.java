package com.huang.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 使用了延迟队列，延迟队列里面其实是一个优先级队列，优先级队列里面是一个二叉树来维持优先级
 *      在延迟队列里面，以延迟的时间作为优先级的值，经过排序后，排在前面就是最近需要执行的消息
 *      获取到该消息后，获得其执行点，如果当前时间<执行点，挂起该线程，如果当前时间>执行点，立刻执行
 * Created on 2018/1/5 17:47
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
@Slf4j
public class ScheduledThreadPoolExectorTest {

    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);

    /**
     * schedule(Runnable command,long delay, TimeUnit unit)：在指定的延迟时间一次性启动任务（Runnable），没有返回值。
     */
    private static void test1(){
        executor.schedule(() -> {
            System.out.println("after 100's");
        }, 1000, TimeUnit.SECONDS);
        executor.shutdown();
    }

    /**
     * scheduleAtFixedRate(Runnable command,long initialDelay,long period,TimeUnit unit)：建并执行一个在给定初始延迟后首次启用的定期操作，
     * 后续操作具有给定的周期；也就是将在 initialDelay 后开始执行，然后在 initialDelay+period 后执行，接着在 initialDelay + 2 * period 后执行，依此类推。
     * 如果任务的任何一个执行遇到异常，则后续执行都会被取消。否则，只能通过执行程序的取消或终止方法来终止该任务。
     * 如果此任务的任何一个执行要花费比其周期更长的时间，则将推迟后续执行，但不会同时执行。
     */
    private static int[] nums = new int[]{5, 1, 2, 3};
    private static volatile int n =0;
    private static void test2(){
        executor.scheduleAtFixedRate(() -> {
            log.info(n + " start");
            try {
                TimeUnit.SECONDS.sleep(nums[n++]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info(n + "end");
        }, 1l, 4l,  TimeUnit.SECONDS);
        try {
            TimeUnit.SECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }

    /**
     * scheduleWithFixedDelay(Runnable command,long initialDelay,long delay,TimeUnit unit)：创建并执行一个在给定初始延迟后首次启用的定期操作，
     * 随后，在每一次执行终止和下一次执行开始之间都存在给定的延迟。如果任务的任一执行遇到异常，就会取消后续执行。否则，只能通过执行程序的取消或终止方法来终止该任务。
     * 这里延迟指的是在上次任务执行结束之后，延迟指定的时间，执行下次任务
     */
    private static void test3(){
        executor.scheduleWithFixedDelay(() -> {
            int t = n;
            log.info(t + " start");
            try {
                TimeUnit.SECONDS.sleep(nums[n++]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info(t + "end");
        }, 1, 4, TimeUnit.SECONDS);
    }


    /**
     * 如果执行失败了，该任务后续就不会继续执行了
     */
    private static void test4(){
        executor.scheduleWithFixedDelay(() -> {
            log.info("start");
//           Integer.valueOf("aaa");
        }, 1, 2, TimeUnit.SECONDS);
        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        test4();
    }
}
