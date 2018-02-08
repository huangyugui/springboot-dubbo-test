package com.huang.oom;

import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试在代码中如何产生堆内存溢出、栈溢出（超出长度）、栈内存溢出（栈不能扩展的情况下OOM）、方法区内存溢出、常量池内存溢出
 * JDK1.7
 *
 * @author Administrator
 */
public class TestOOM {
    private static int count = 1;
    private static final int _1MB = 1024 * 1024;

    List<String> list = new ArrayList<String>();

    //一个普通的对象  
    static class OOMObjectClass {
        public OOMObjectClass() {
        }
    }

    /**
     * 通过list对象保持对对象列表的引用，不然GC收集对象，然后不断地向列表中添加新的对象，就会发生OOM
     *
     * @VM args:-verbose:gc -Xms10M -Xmx10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:+HeapDumpOnOutOfMemoryError
     */
    public void testHeapOOM() {
        List<OOMObjectClass> list = new ArrayList<>();
        while (true) {
            list.add(new OOMObjectClass());
        }
    }

    /**
     * 通过递归调用方法，从而让方法栈产生栈 StackOverflowError
     *
     * @VM args:-verbose:gc -Xss128k
     */
    public void stackLeak() {
        count++;
        stackLeak();
    }


    /**
     * 除了上述的递归调用可以产生溢出外，还有就是过多的线程，当栈内存无法动弹扩展是，会出现OOM
     * <p>
     * 由于在Window的JVM中，Jave的线程是映射到了操作系统的内核线程上，故而这段代码的运行时非常危险的
     * 笔者运行的时候限制了JVM内存大小，但是栈内存可以动态扩展，所以电脑内存直接到了90%以上，我果断停止了程序的运行
     * 由于栈内存只由-Xss参数控制，并没有办法让其不自动扩展，所以这段代码非常危险
     * 参数：-verbose:gc -Xms10M -Xmx10M -Xss2M
     */
    public void stackLeakByThread() {
        while (true) {
            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    while (true) {

                    }
                }
            });
            t.start();
            count++;
        }
    }

    /**
     * 常量池是存在于方法区内的，故而只要限制了方法区的大小，当不断新增常量的时候就会发生常量池的溢出
     * <p>
     * 笔者使用的是JDK1.7 64位，此时的常量池已经不存在与方法区中，而是迁移到了堆中，故而测试的时候需要限制JVM的堆大小，且不能自动扩展
     *
     * @VM args: -Xms10M -Xmx10M
     */
    public void constantPoolOOM() {
        int i = 0;
        while (true) {
            list.add(String.valueOf(i++).intern()); //String类型的intern方法是将字符串的值放到常量池中  
        }
    }

    /**
     * 方法区是存放一些类的信息等，所以我们可以使用类加载无限循环加载class，这样就会出现方法区的OOM异常
     * 主要，使用内部类的时候，需要要使用静态内部类，如果使用的是非静态内部类，将不会发生方法区OOM
     * 使用了CGLib直接操作字节码运行时，生成了大量的动态类
     * 需要者两个jar包：cglib-2.2.2.jar   asm-3.1.jar
     *
     * @VM args:-XX:PermSize=10M -XX:MaxPermSize=10M
     */
    public void methodAreaOOM() {
        while (true) {
//            Enhancer eh = new Enhancer();
//            eh.setSuperclass(OOMObjectClass.class);
//            eh.setUseCache(false);
//            eh.setCallback(new MethodInterceptor() {
//                @Override
//                public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable {
//                    return arg3.invokeSuper(arg0, arg2);
//                }
//            });
//            eh.create();
        }
    }

    /**
     * 要讨论这部分的内存溢出，首先必须要说一下什么是直接内存：
     * 直接内存并不是JVM运行时数据区的一部分，也不是JVM规范中定义的内存区域，但是这部分内存也被频繁的使用，也会产生OOM。
     * JDK1.4中新加入了NIO类，引入了一种Channel与Buffer的I/O方式，它可以使用Native函数库直接分配堆外内存，然后通过一个存储在JAVA堆里面的DirectByteBuffer对象作为
     * 这些堆外内存的引用进而操作，这样在某些场景中可以显著的提高性能，避免了在native堆和java堆中来回复制数据。这这部分堆外内存就是直接内存了。
     * <p>
     * 直接内存虽然不会受到JAVA堆大小的限制，但是还是会受到本机内存大小的限制，故而服务器管理员在设置JVM内存管理参数的时候，如果忘记了直接内存，那么当程序进行动态扩展的时候，就有可能发生OOM
     * 直接内存的容量可以通过-XX：MaxDirectMemorySize指定，如果不指定，那么默认与JAVA堆得最大值一样。
     *
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @VM args:-Xmx20M -XX:MaxDirectMemorySize=10M
     */
    public void directMemoryOOM() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        while (true) {
            unsafe.allocateMemory(_1MB);
        }
    }


    public static void main(String[] args) {
        TestOOM oom = new TestOOM();
//      ---------测试堆内存溢出-----------  
//      oom.testHeapOOM();

//      ---------测试栈溢出----------  
      try{
          oom.stackLeak();
      }catch(Throwable error){
          System.out.println("Stack length-->"+count);
          throw error;
      }

//      ---------测试由于栈动态扩展导致的OOM----------        
//      try{  
//          oom.stackLeakByThread();  
//      }catch(Throwable error){  
//          System.out.println("Stack length-->"+count);  
//          throw error;  
//      }  

//      ----------测试方法区溢出----------  
//      oom.methodAreaOOM();  

//      ----------测试常量池溢出----------  
//      oom.constantPoolOOM();  

//      ----------测试直接内存溢出----------  

        try {
            oom.directMemoryOOM();
        } catch (Exception e) {
            System.out.println(e);
        }


    }


}  