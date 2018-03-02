package com.huang.erchaTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:  二叉树排序
 * Created on 2018/3/2 18:47
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class BinaryTreeOrder {

    public static void main(String[] args){

        int[] nums = new int[]{5, 9, 16, 3, 7, 19, 2, 10};
        TNode root = new TNode(null, null, 5);
        for (int i = 1; i < nums.length; i++) {
            insert(root, nums[i]);
        }
        List<Integer> list = new ArrayList<>();
        list(root, list);
        list.stream().forEach(item -> System.out.println(item));
    }

    private static void insert(TNode root, int num){

        TNode currentNode = root;
        boolean insertOk = false;
        while(!insertOk){
            if(currentNode.getV() > num){
                if(currentNode.getLeft() == null){
                    currentNode.setLeft(new TNode(null, null, num));
                    insertOk = true;
                }
                currentNode = currentNode.getLeft();
            }else{
                if(currentNode.getRight() == null){
                    currentNode.setRight(new TNode(null, null, num));
                    insertOk = true;
                }
                currentNode = currentNode.getRight();
            }
        }

    }

    private static void list(TNode node, List<Integer> list){
        if(node == null){
            return;
        }

        list(node.getLeft(), list);
        list.add(node.getV());
        list(node.getRight(), list);
    }

}
