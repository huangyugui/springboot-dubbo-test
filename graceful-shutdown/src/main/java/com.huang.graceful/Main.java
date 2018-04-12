package com.huang.graceful;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * Description:到处可执行jar的方法：
 * https://www.cnblogs.com/blog5277/p/5920560.html
 *
 * 可以采用此种方式，先关闭接收请求（Signal）
 * 然后在执行完毕当前已接收的请求（通过addShutdownHook，比如程序使用线程去处理请求，可以使用shutdown，而不是shutdownNow）
 *
 * 参考：https://zhuanlan.zhihu.com/p/23713953  Java消息队列任务的平滑关闭
 *      https://my.oschina.net/lujianing/blog/787745  Java消息队列任务的平滑关闭
 *      https://www.jianshu.com/p/aa22eac09d8c    Dubbo 优雅停机
 *
 *
 * Created on 2018/4/11 10:38
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class Main {

    private static volatile boolean running = true;

    public static void main(String[] args){
        synchronized (Main.class) {
            while (running) {
                try {
                    System.out.println("start....");
                    Main.class.wait();
                } catch (Throwable e) {
                }
            }
        }
    }

    static {
        /**
         * SIGTERM,SIGINT,SIGHUP三种信号都会触发该方法
         * （分别对应kill -1/kill -2/kill -15，Ctrl+c也会触发SIGINT信号
         */
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                System.out.println("shutdownHook");
            }
        });

        /**
         * 通过Signal类注册信号监听，比如USR2（kill -12），在handle方法中执行操作
         */
        Signal sig = new Signal("USR2");
        Signal.handle(sig, new SignalHandler() {
            @Override
            public void handle(Signal signal) {
                System.out.println("USR2 kill");
            }
        });
    }


}
