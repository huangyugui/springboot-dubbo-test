package com.huang.pattern.strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Created on 2018/3/17 18:36
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class PricePrint {

    private  static Map<String, CalculatePrice> map = new HashMap();
    static {
        map.put("student", new StudentPrice());
        map.put("member", new MemberPrice());
    }

    public static void main(String[] args){
        Student s = new Student();
        s.setName("小明");

        String className = s.getClass().getName().substring(s.getClass().getName().lastIndexOf(".") + 1).toLowerCase();
        System.out.println(className);
        CalculatePrice cp = map.get(className);
        cp.calculate();
    }

}
