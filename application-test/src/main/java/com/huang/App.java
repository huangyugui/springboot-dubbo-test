package com.huang;

import lombok.extern.slf4j.Slf4j;

/**
 * Hello world!
 *
 */
@Slf4j
public class App {






//    private static ThreadPoolTaskExecutor executor;
//
//    static {
//        executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(2);
//        executor.setKeepAliveSeconds(300);
//        executor.setMaxPoolSize(5);
//        executor.setQueueCapacity(5);
//        executor.initialize();
//    }
//
//
//    public static void main( String[] args ) {
//
//        for(int i = 0; i < 11; i++){
////            try {
////                if(i == 10){
////                    TimeUnit.SECONDS.sleep(20);
////                }else{
//////                    TimeUnit.SECONDS.sleep(3);  z
////                }
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
//            executor.submit(() -> {
//                log.info("开始执行");
////                Integer.valueOf("aa");
////                try {
////                    TimeUnit.SECONDS.sleep(8);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
//                try {
//                    Integer.valueOf("aa");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//
//    }
}
