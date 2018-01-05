package com.huang.executor;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Created on 2018/1/5 17:47
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class ScheduledThreadPoolExectorTest {

    private static void test1(){
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
        executor.schedule(() -> {
            System.out.println("after 100's");
        }, 1000, TimeUnit.SECONDS);
        executor.shutdown();
    }

    public static void main(String[] args){
        test1();
    }
}
