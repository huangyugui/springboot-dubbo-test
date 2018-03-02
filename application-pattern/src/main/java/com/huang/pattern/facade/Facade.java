package com.huang.pattern.facade;

/**
 * Description:
 * Created on 2018/2/27 17:56
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class Facade {

    private SubSystem1 s1 = new SubSystem1();
    private SubSystem2 s2 = new SubSystem2();
    private SubSystem3 s3 = new SubSystem3();

    public void method(String name1, String name2){
        s1.method(name1);
        s2.method();
        s3.method(name2);
    }

}

class SubSystem1{
    public void method(String name){
        System.out.println(name);
    }
}

class SubSystem2{
    public void method(){
        System.out.println("----------");
    }
}

class SubSystem3{
    public void method(String name){
        System.out.println(name);
    }
}