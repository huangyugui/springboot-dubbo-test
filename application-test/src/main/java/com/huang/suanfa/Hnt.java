package com.huang.suanfa;

/**
 * Description: 汉诺塔问题：https://www.zhihu.com/question/24385418/answer/282940567
 * 三个栈，只能把数据放到顶部，并且只能从顶部取数据
 * 一个栈from是有一串顺序的数字，将这串数字移到第三个栈to上，以中间为buff
 * 可以这么思考：如果一个栈上数字为：1、2、3、4
 * 那么第一步将1、2、3 移到buff，
 * 然后将4 移动到to上
 * 然后将1、2、3 移动到to上就完成了，而将1、2、3移动到buff上只需要前面通过类似的递归就可以完成
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
