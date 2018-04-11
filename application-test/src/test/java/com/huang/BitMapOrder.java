package com.huang;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:采用bitMap来排序
 * 1、数字不能重复
 * 2、其实采用的是二维数组，第二维的数组用一个2^32来表示，因此就是一个int
 * 参考：https://blog.csdn.net/sunnyyoona/article/details/43604387
 * Created on 2018/3/29 20:40
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class BitMapOrder {

    private static int index = 10;

    //可以承受的数字范围是0 ~ 3200
    private static int[] a = new int[index];

    public static void add(int num) {
        //算出在一维中的第几个,向右移5位，相当于除于32
        int row = num >> 5;
        /**
         * num & 0x1F ： 0x1F是16进制中的32，num & 32 相当于 num % 32 = index
         * 1 << index 表示向左移动 index位，一个int用二进制表示正好是 32位
         * 因此index是多少，就在第多少位上面设置为1
         * 最后 |= 表示将原来数组的值加上后面的值
         */
        a[row] |= 1 << (num & 0x1F);
    }

    public static void show() {
        System.out.println("bitMap 展示：");
        for (int i = 0; i < index; i++) {
            System.out.print("a[" + i + "]:");
            int tmp = a[i];
            for (int j = 0; j < 32; j++) {
                System.out.print(tmp & 1);
                System.out.print(" ");
                tmp = tmp >> 1;
            }
            System.out.println();
        }
    }

    public static void order(){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < index; i++) {
            int tmp = a[i];
            for (int j = 0; j < 32; j++) {
                int num = tmp & 1;
                if(num == 1){
                    list.add(i << 5 | j);
                }
                tmp = tmp >> 1;

            }
        }
        list.stream().forEach(System.out::println);
    }

    public static void main(String[] args) {
        int[] a = new int[5];
        a[0] = 10;
        a[1] = 3;
        a[2] = 6;
        a[3] = 18;
        a[4] = 1;
        for (int i = 0; i < a.length; i++) {
            add(a[i]);
        }
        show();
        order();
    }

}
