package com.huang.failFast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class FastFailTest {

    private static List<String> list = new ArrayList<String>();
    static CountDownLatch c = new CountDownLatch(1);

    //private static List<String> list = new CopyOnWriteArrayList<String>();
    public static void main(String[] args) throws InterruptedException {

        // 同时启动两个线程对list进行操作！

        new ThreadOne().start();
        new ThreadTwo().start();
        TimeUnit.SECONDS.sleep(1);
        c.countDown();
    }

    private static void printAll() {
        System.out.println("");

        String value = null;
        //会产生fail-fast
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            value = (String) iter.next();
            System.out.print(value + ", ");
        }
        //不会产生fail-fast
        for (int i = 0; i < list.size(); i++){
            System.out.print(list.get(i));
        }
    }

    /**
     * 向list中依次添加0,1,2,3,4,5，每添加一个数之后，就通过printAll()遍历整个list
     */
    private static class ThreadOne extends Thread {
        public void run() {
            try {
                c.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int i = 0;
            while (i < 600) {
                list.add(String.valueOf(i));
                printAll();
                i++;
            }
        }
    }

    /**
     * 向list中依次添加10,11,12,13,14,15，每添加一个数之后，就通过printAll()遍历整个list
     */
    private static class ThreadTwo extends Thread {
        public void run() {
            try {
                c.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int i = 10;
            while (i < 16) {
                list.add(String.valueOf(i));
                printAll();
                i++;
            }
        }
    }

}