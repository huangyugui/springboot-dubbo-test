package com.huang.thread.atomic;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

@Slf4j
public class AtomicIntegerFieldUpdaterDemo {

    static class DemoData {
        public volatile int value1 = 1;
        //字段必须是 volatile的，且不能是final的
        volatile int value2 = 2;
        protected volatile int value3 = 3;
        private volatile int value4 = 4;

        public volatile int[] arr = new int[]{1, 2, 3, 4};
    }

    AtomicIntegerFieldUpdater<DemoData> getUpdater(String fieldName) {
        return AtomicIntegerFieldUpdater.newUpdater(DemoData.class, fieldName);
    }

    void doit() {
        //只能是实例的字段，不能是类的字段(static)
        DemoData data = new DemoData();
        System.out.println("1 ==> " + getUpdater("value1").getAndSet(data, 10));
        System.out.println("3 ==> " + getUpdater("value2").incrementAndGet(data));
        //DemoData的value3/value4字段的修饰符protected/private导致这两个字段对于类AtomicIntegerFieldUpdaterDemo 来说是不可见的，
        // 因此不能够通过AtomicIntegerFieldUpdater来修改此值。因此运行时下面两句会得到一个非法访问的异常。
//        System.out.println("2 ==> " + getUpdater("value3").decrementAndGet(data));
//        System.out.println("true ==> " + getUpdater("value4").compareAndSet(data, 4, 5));
        getUpdater("arr").compareAndSet(data, data.arr[2], 10);
        System.out.println(data.arr);
    }

    public static void main(String[] args) {
        AtomicIntegerFieldUpdaterDemo demo = new AtomicIntegerFieldUpdaterDemo();
        demo.doit();

        try {
            DemoData data = new DemoData();
            Field f = DemoData.class.getDeclaredField("value3");
            int tt = (int) f.get(data);
            log.info("value3: {}", tt);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }



    }
}

