package com.huang.pattern.facade;

/**
 * Description: 外观模式
 *      某个功能需要调用多个复杂的子系统来实现，而且客户端与多个子系统存在严重的依赖性，可以使用外观模式来解耦从而提高子系统的独立性和可移植性，
 *      例如在Dubbo中服务提供方这里使用外观模式，组合多个服务
 *      目前此外观模式是不符合开闭原则的，可以引入抽象外观类来解决此问题
 *
 * Created on 2018/2/27 18:00
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class Client {

    public static void main(String[] args){
        Facade f = new Facade();
        f.method("aaaa", "bbbbb");
    }

}
