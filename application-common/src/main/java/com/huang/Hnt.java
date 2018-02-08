package com.huang;

/**
 * Description: 汉诺塔问题：
 * Created on 2018/1/23 10:28
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class Hnt {

    public static void move(int n, String from, String buffer, String to){
        if(n == 1) {
            System.out.println("move " + n + " from " + from + " to " + to);
        }else {
            move(n-1, from, to, buffer);
            System.out.println("move " + n + " from " + from + " to " + to);
            move(n-1, buffer, from, to);
        }
    }

    public static void main(String[] args){
        move(4, "A", "B", "C");
    }

}
