package com.huang.suanfa;

import java.util.Stack;

/**
 * Description: 题目描述：
 * 两个栈，一个栈里面有数字，另一个栈是空，从而来达到排序
 * 解法要点：使用一个临时变量，然后将数据两边来回倒来倒去就可以了
 * Created on 2018/3/11 10:44
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class TwoStackOrder {

    public static void main(String[] args){
        Stack<Integer> stack = new Stack<>();
        stack.push(3);
        stack.push(2);
        stack.push(5);
        stack.push(1);
        stack = order(stack);
        stack.stream().forEach(item -> System.out.println(item));

    }

    private static Stack<Integer> order(Stack<Integer> stack){
        //辅助栈
        Stack<Integer> help = new Stack();
        while(!stack.empty()){
            //临时变量
            Integer top = stack.pop();
            if(help.empty() || help.peek() >= top){
                help.push(top);
                continue;
            }

            //如果临时变量比辅助栈的首元素大，就把辅助栈里面的元素再倒回去
            while(!help.empty() && help.peek() < top){
                stack.push(help.pop());
            }
            help.push(top);
        }
        return help;
    }

}
