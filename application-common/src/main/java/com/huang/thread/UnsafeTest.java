package com.huang.thread;

import lombok.Getter;
import lombok.Setter;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Description:
 * Created on 2017/11/20 18:17
 *
 * @author <a href="mailto: yugui_huang0305@163.com">黄渝贵</a>
 * @version 1.0
 *
 */
public class UnsafeTest {

    private static Unsafe unsafe;

    static {
        try {
            unsafe = getUnsafe();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

//    private static final Unsafe unsafe = Unsafe.getUnsafe();

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        password();
    }

    public static void password(){
        String password = new String("l00k@myHor$e");
        String fake = new String(password.replaceAll(".", "?"));
        System.out.println(password); // l00k@myHor$e
        System.out.println(fake); // ????????????

        unsafe.copyMemory(
                fake, 0L, null, toAddress(password), sizeOf(password));

        System.out.println(password); // ????????????
        System.out.println(fake); // ????????????
    }

    public static long sizeOf(Object object) {
        return unsafe.getAddress(
                normalize(unsafe.getInt(object, 4L)) + 12L);
    }

    private static long normalize(int value) {
        if (value >= 0) return value;
        return (~0L >>> 32) & value;
    }

    static long toAddress(Object obj) {
        Object[] array = new Object[]{obj};
        long baseOffset = unsafe.arrayBaseOffset(Object[].class);
        return normalize(unsafe.getInt(array, baseOffset));
    }

    static Object fromAddress(long address) {
        Object[] array = new Object[]{null};
        long baseOffset = unsafe.arrayBaseOffset(Object[].class);
        unsafe.putLong(array, baseOffset, address);
        return array[0];
    }

    private static void initA() {
        try {
            A a = (A) unsafe.allocateInstance(A.class);
            System.out.println(a.getA());
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }


    private static void test() {
//        Unsafe unsafe = Unsafe.getUnsafe();
//        Unsafe unsafe = getUnsafe();
        System.out.println(unsafe.addressSize());
        System.out.println(unsafe.pageSize());
    }

    /**
     * Unsafe
     */
    public static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);
        return unsafe;
    }


}

@Setter
@Getter
class A {
    private long a; // not initialized value

    public A() {
        this.a = 1; // initialization
    }

}
