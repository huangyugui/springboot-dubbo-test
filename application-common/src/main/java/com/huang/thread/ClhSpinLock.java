package com.huang.thread;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Description: 自旋锁，利用ThreadLocal来实现，两个ThreadLocal里分别存储该线程的 当前节点和前置节点
 *      当lock时，获取当前节点，并将当前节点的locked属性设置为true，然后获取前置节点
 *      (在ClhSpinLock中使用tail保存锁当前的状态，tail里面是一个boolean的属性，用来表示当前锁是否处于锁定状态，
 *      另外tail里面始终存放着最后一个想获取锁的Node，每次有节点想获取锁，就可以通过这个变量获取前置节点，并将tail
 *      的值修改为自己，以便后续获取想获取锁的节点获取前置节点)
 *      循环前置节点的locked值，当值变为false时，就轮到当前节点拿到锁了
 *
 *      这种算法有一个缺点是在NUMA系统架构下性能表现很差，因为它自旋的locked是前驱线程的，
 *      如果内存位置较远，那么性能会受到损失。但是在SMP这种cache一致性的系统架构上表现良好。
 *
 * 1.初始状态 tail指向一个node(head)节点
 * +------+
 * | head | <---- tail
 * +------+
 *
 * 2.lock-thread加入等待队列: tail指向新的Node，同时Prev指向tail之前指向的节点
 * +----------+
 * | Thread-A |
 * | := Node  | <---- tail
 * | := Prev  | -----> +------+
 * +----------+        | head |
 *                     +------+
 *
 *             +----------+
 *             | Thread-B |            +----------+
 * tail ---->  | := Node  |            | Thread-A |
 *             | := Prev  | ---------> | := Node  |
 *             +----------+            | := Prev  | ----->  +------+
 *                                     +----------+         | head |
 *	                                                        +------+
 * 3.寻找当前node的prev-node然后开始自旋
 *
 *
 * Created on 2017/11/29 11:54
 * @version 1.0
 */
public class ClhSpinLock {

    private final ThreadLocal<Node> prev;
    private final ThreadLocal<Node> node;
    private final AtomicReference<Node> tail = new AtomicReference<Node>(new Node());

    public ClhSpinLock() {
        this.node = new ThreadLocal<Node>() {
            protected Node initialValue() {
                return new Node();
            }
        };

        this.prev = new ThreadLocal<Node>() {
            protected Node initialValue() {
                return null;
            }
        };
    }

    public void lock() {
        final Node node = this.node.get();
        node.locked = true;
        // 一个CAS操作即可将当前线程对应的节点加入到队列中，
        // 并且同时获得了前继节点的引用，然后就是等待前继释放锁
        Node pred = this.tail.getAndSet(node);
        this.prev.set(pred);
        int i = 1;
        while (pred.locked) {// 进入自旋
            try {
                Thread.sleep(100);
                System.out.println(Thread.currentThread().getId() + "自旋次数：" + i++);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getId() + "自旋结束，总次数：" + i);
    }

    public void unlock() {
        final Node node = this.node.get();
        node.locked = false;
        //将当前node指向前驱node(这样操作tail指向的就是前驱node等同于出队操作).至此CLH Lock的过程就结束了
        this.node.set(this.prev.get());
    }

    private static class Node {
        private volatile boolean locked;
    }

    public static void main(String[] args) throws InterruptedException {
        final ClhSpinLock lock = new ClhSpinLock();
        lock.lock();

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    lock.lock();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
