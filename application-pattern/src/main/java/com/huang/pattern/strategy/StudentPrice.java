package com.huang.pattern.strategy;

/**
 * Description:
 * Created on 2018/3/17 18:35
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class StudentPrice implements CalculatePrice {
    @Override
    public void calculate() {
        System.out.println("学生大5折");
    }
}
