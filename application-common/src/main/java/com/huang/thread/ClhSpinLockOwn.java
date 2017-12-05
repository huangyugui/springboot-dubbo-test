package com.huang.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Description:
 * Created on 2017/11/29 15:29
 *
 * @author <a href="mailto: yugui_huang0305@163.com">黄渝贵</a>
 * @version 1.0
 *
 */
@Slf4j
public class ClhSpinLockOwn {

    static class Nodes{
        private Node pre;
        private Node curr;
    }

    static class Node{
        private volatile boolean locked = false;
    }

    ThreadLocal<Nodes> threadLocal;

    //用来指向最后一个想获取锁的节点，通过这个变量，就将所有的想获取锁的对象连成了一个链
    AtomicReference<Node> tail = new AtomicReference<>(new Node());

    public ClhSpinLockOwn(){
        threadLocal = new ThreadLocal<Nodes>(){
            @Override
            protected Nodes initialValue() {
                Nodes ns = new Nodes();
                ns.pre = new Node();
                ns.curr = new Node();
                return ns;
            }
        };
    }

    public void lock(){
        Nodes ns = threadLocal.get();
        ns.curr.locked = true;
        //将当前节点设置为链的最后一个节点
        tail.getAndSet(ns.curr);
        Node pre = ns.pre;
        int i = 1;
        while(pre.locked){
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
        Nodes ns = threadLocal.get();
        ns.curr.locked = false;
    }

    public static void main(String[] args) throws InterruptedException {
        final ClhSpinLock lock = new ClhSpinLock();
        lock.lock();

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    lock.lock();
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
