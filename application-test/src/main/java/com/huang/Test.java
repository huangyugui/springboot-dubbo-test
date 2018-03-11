package com.huang;

import java.util.Arrays;

/**
 * Description:
 * Created on 2018/3/1 23:23
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class Test {

    public static void main(String[] args){
        int[] nums = new int[]{1, 3, 4, 2, 7, 11, 15};
        nums = twoSum(nums, 9);
        for(int i = 0; i < nums.length; i++){
            System.out.println(nums[i]);
        }
    }

    public static int[] twoSum(int[] nums, int target) {
        int num1 = 0;
        int num2 = 0;
        for(int i =0; i < nums.length; i++){

            if(nums[i] < target){
                num1 = nums[i];
                for(int j = 0; j < nums.length; j++){
                    if(nums[j] < target && i != j && nums[i] + nums[j] == target){
                        return new int[]{i, j};
                    }
                }
            }
        }
        return null;
    }

}
