package com.huang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by LiJun on 2017/11/2.
 */
@RequestMapping("/thread")
public class ThreadController {

    @Autowired
    private ThreadPoolExecutor executor;

    @RequestMapping("test")
    public void threadPoolTest(){
        for(int i = 0; i < 30; i++){
            executor.execute(() -> {

                try {
                    TimeUnit.SECONDS.sleep(1l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Integer.valueOf("aaa");

            });
        }
    }
}
