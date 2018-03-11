package com.huang.suanfa;

/**
 * Description:
 * Created on 2018/3/11 11:48
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class TailDigui {

    public static void main(String[] args){
        sum(999999999, 0);
    }

    public static long sum(long num, long sum){
        if(num == 1l){
            return sum;
        }
        sum += num;
        return sum(num - 1, sum);
    }
}
