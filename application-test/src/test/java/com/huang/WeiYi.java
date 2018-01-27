package com.huang;

import com.sun.org.apache.xpath.internal.SourceTree;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 * Created on 2017/11/16 19:04
 *
 * @author <a href="mailto: yugui_huang0305@163.com">黄渝贵</a>
 * @version 1.0
 *
 */
public class WeiYi {

    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;
    private static final int RUNNING    = -1 << COUNT_BITS;
    private static final int SHUTDOWN   =  0 << COUNT_BITS;
    private static final int STOP       =  1 << COUNT_BITS;
    private static final int TIDYING    =  2 << COUNT_BITS;
    private static final int TERMINATED =  3 << COUNT_BITS;

    public static void main(String[] args){
        test4();
    }

    private static void test4(){
        int h = 33;
        int HASH_BITS = 0x7fffffff;
        int r = (h ^ (h >>> 16)) & HASH_BITS;
        System.out.println(r);
    }

    private static void test3(){
        System.out.println(Integer.toBinaryString(16));
        print(16 >>> 3);

        int SHARED_SHIFT   = 16;
        print((1 << SHARED_SHIFT));
        print((1 << SHARED_SHIFT) - 1);
        print(200 >>> SHARED_SHIFT);
        print(2 & (1 << SHARED_SHIFT) - 1);
    }

    private static void test2(){
        print(CAPACITY);
        print(~CAPACITY);
        print(RUNNING & ~CAPACITY);
        print(RUNNING & CAPACITY);
        print(RUNNING | CAPACITY);

        final Unsafe unsafe = Unsafe.getUnsafe();
        try {
            long valueOffset = unsafe.objectFieldOffset
                    (AtomicInteger.class.getDeclaredField("value"));
            System.out.println(valueOffset);
        } catch (Exception ex) { throw new Error(ex); }
    }

    private static void print(int i){
        System.out.println(i);
        System.out.println(Integer.toBinaryString(i));
        System.out.println();
    }

    private static void test1(){
        int num = 2147483647;
        for(int i = 0; i < 35; i++){
            num = num >> 1;
            System.out.println(Integer.toBinaryString(num)  + "<---->" + num);
        }

        System.out.println(11 | 11);
        System.out.println(-1 << Integer.SIZE - 3);
        System.out.println(Integer.toBinaryString(-1 << Integer.SIZE - 3));
        System.out.println(0 << Integer.SIZE - 3);
        System.out.println(Integer.toBinaryString(0 << Integer.SIZE - 3));
        System.out.println(1 << Integer.SIZE - 3);
        System.out.println(Integer.toBinaryString(1 << Integer.SIZE - 3));
        System.out.println(2 << Integer.SIZE - 3);
        System.out.println(Integer.toBinaryString(2 << Integer.SIZE - 3));
        System.out.println(3 << Integer.SIZE - 3);
        System.out.println(Integer.toBinaryString(3 << Integer.SIZE - 3));
        System.out.println(~2);
        Integer fei = Integer.parseInt("00000000000000000000000000000001", 10);
        System.out.println(fei);
        System.out.println(~fei);
        System.out.println(Integer.toBinaryString(~fei));
        System.out.println(Integer.toBinaryString(-3));
    }

}

class Person{
    String name;
    String age;

    @Override
    public int hashCode() {
        return Objects.hashCode(name) + Objects.hashCode(age);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        Person p = (Person)obj;
        if(this.name.equals(p.name) && this.age.equals(p.age))
            return true;
        return false;
    }
}
