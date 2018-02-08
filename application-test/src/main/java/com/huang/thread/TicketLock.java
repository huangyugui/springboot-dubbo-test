package com.huang.thread;

import java.util.concurrent.atomic.AtomicInteger;

public class TicketLock {
    private AtomicInteger serviceNum = new AtomicInteger(); // 服务号
    private AtomicInteger ticketNum = new AtomicInteger(); // 排队号

    public int lock() { // 首先原子性地获得一个排队号
        int myTicketNum = ticketNum.getAndIncrement(); // 只要当前服务号不是自己的就不断轮询
        while (serviceNum.get() != myTicketNum) {
        }
        return myTicketNum;
    }

    public void unlock(int myTicket) { // 只有当前线程拥有者才能释放锁
        int next = myTicket + 1;
        serviceNum.compareAndSet(myTicket, next);
    }

    public static void main(String[] args) throws InterruptedException {
        final TicketLock lock = new TicketLock();
        int num = lock.lock();

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int n = lock.lock();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getId() + " acquired the lock!");
                    lock.unlock(n);
                }
            }).start();
        }
        Thread.sleep(300);
        System.out.println("main thread unlock!");
        lock.unlock(num);
    }
}