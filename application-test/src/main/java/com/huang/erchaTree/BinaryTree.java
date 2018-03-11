package com.huang.erchaTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *    参考：http://blog.csdn.net/cheidou123/article/details/78187666
 *    https://www.61mon.com/index.php/archives/191/
 * Created on 2018/3/2 15:20
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class BinaryTree {

    static TNode root;
    static{
        TNode node1 = new TNode(null, null, 1);
        TNode node4 = new TNode(null, null, 4);
        TNode node3 = new TNode(node1, node4, 3);

        TNode node8 = new TNode(null, null, 8);
        TNode node15 = new TNode(null, null, 15);
        TNode node9 = new TNode(node8, node15, 9);
        TNode node7 = new TNode(null, node9, 7);
        root = new TNode(node3, node7, 5);
    }


    private static int maxdeep(TNode node){
        if(node == null) return 0;
        int left = maxdeep(node.getLeft());
        int right = maxdeep(node.getRight());
        return 1 + Math.max(left, right);
    }

    private static int mindeep(TNode node){
        if(node == null) return 0;
        int left = mindeep(node.getLeft());
        int right = mindeep(node.getRight());
        //当left或者right == 0时，此时肯定该节点的左节点或者右节点为空
        //假设左节点为空，那么left =0；那么如果右节点不为空，那么right 就等于当前节点的深度，
        // 因此left + right + 1 表示当前节点的深度
        //如果left和right都不为空，则求当前节点的左节点或者右节点的最小深度，+1表示从当前节点开始的最小节点
        return  (left==0||right==0) ? left + right + 1 : Math.min(left,right)+1;
    }

    private static TNode invert(TNode node){
        if(node == null) return node;
        TNode tmp = node.getLeft();
        node.setLeft(node.getRight());
        node.setRight(node.getLeft());
        invert(node.getLeft());
        invert(node.getRight());
        return node;
    }

    private static boolean isEquals(TNode n1, TNode n2){
        if(n1 == null && n2 == null) return true;
        if(n1 != null || n2 != null) return false;

        if(n1.getV() == n2.getV() && isEquals(n1.getLeft(), n2.getLeft()) && isEquals(n2.getRight(), n2.getRight()))
            return true;
        else
            return false;
    }

    private static int leftSum(TNode node){
        if(node == null) return 0;
        int sum = 0;
        if(node.getLeft() != null){
            sum += node.getLeft().getV() + leftSum(node.getLeft());
        }
        sum += leftSum(node.getRight());
        return sum;
    }

    private static List<List<TNode>> father = new ArrayList<List<TNode>>();
    private static List<TNode> child = new ArrayList<TNode>();
    private static List<List<TNode>> sumEquals(TNode node, int target){
        if(node == null) return father;

        child.add(node);
        target -= node.getV();

        if(node.getLeft() == null && node.getRight() == null && target == 0){
            father.add(child);
        }
        sumEquals(node.getLeft(), target);
        sumEquals(node.getRight(), target);
        child.remove(child.size()-1);   //关键3
        return father;
    }

    private static void qianOrder(TNode node, List<Integer> list){
        if(node == null) return;

        list.add(node.getV());
        qianOrder(node.getLeft(), list);
        qianOrder(node.getRight(), list);
    }


    private static void zhongOrder(TNode node, List<Integer> list){
        if(node == null) return;

        zhongOrder(node.getLeft(), list);
        list.add(node.getV());
        zhongOrder(node.getRight(), list);
    }

    private static void houOrder(TNode node, List<Integer> list){
        if(node == null) return;

        houOrder(node.getLeft(), list);
        houOrder(node.getRight(), list);
        list.add(node.getV());
    }

    //层序遍历：层序遍历就是按照二叉树的层次由上到下的进行遍历，每一层要求访问的顺序为从左到右；
    private static void cengOrder(TNode node, List<Integer> list){
        List<TNode> tmp = new ArrayList<>();
        List<TNode> tmp1;
        tmp.add(node);
        while(tmp.size() != 0){
            tmp1 = new ArrayList<>();
            for(TNode n : tmp){
                if(n == null) continue;
                list.add(n.getV());
                tmp1.add(n.getLeft());
                tmp1.add(n.getRight());
            }
            tmp = tmp1;
        }
    }

    private static void allPath(TNode node, String path, List<String> list){
        if(node ==null) {
            return ;
        }

        if(node.getLeft() == null && node.getRight() == null){
            list.add(path + "->" + node.getV());
            return;
        }
        allPath(node.getLeft(), path + "->" + node.getV(), list);
        allPath(node.getRight(), path + "->" + node.getV(), list);
    }

    private static int leafNum(TNode node){
        if(node ==null) {
            return 0;
        }

        if(node.getLeft() == null && node.getRight() == null){
            return 1;
        }
        int l = leafNum(node.getLeft());
        int r = leafNum(node.getRight());
        return l + r;
    }

    public static void main(String[] args){
//        System.out.println(leftSum(root));
//        sumEquals(root, 9);
//        father.stream().forEach(item -> {
//            item.stream().forEach(t -> System.out.println(t));
//        });

//        List<Integer> list = new ArrayList<>();
//        qianOrder(root, list);
//        list.stream().forEach(item -> System.out.println(item));


//        List<String> list = new ArrayList<>();
//        allPath(root, "", list);
//        list.stream().forEach(item -> System.out.println(item));
        Integer num = leafNum(root);
        System.out.println(num);
    }

}
