package com.huang.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * Description:
 * Created on 2018/3/1 15:55
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class CreateSessionSample {

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework curator = CuratorFrameworkFactory.newClient("localhost:2291",
                5000, 3000, retryPolicy);

        curator.start();

        delete(curator, "/a");
        delete(curator, "/a1");

        /** create  **/
        //默认情况下创建一个持久节点
        curator.create().forPath("/a", "init".getBytes());
        //临时节点
        curator.create().withMode(CreateMode.EPHEMERAL).forPath("/a/b", "/a/b".getBytes());
        //临时节点不能是父节点：KeeperErrorCode = NoChildrenForEphemerals for /a/b/c
//        curator.create().withMode(CreateMode.EPHEMERAL).forPath("/a/b/c", "/a/b/c".getBytes());
        curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/a1/b1/c1");

        /** getData  **/
        Stat stat = new Stat();
        byte[] bytes = curator.getData().storingStatIn(stat).forPath("/a/b");
        System.out.println("/a/b data : " + new String(bytes));
        System.out.println(stat.toString());

        /** setData  **/
        Stat stat1 = curator.setData().withVersion(stat.getVersion()).forPath("/a/b", "update /a/b".getBytes());
        System.out.println(stat1);

        Thread.sleep(Integer.MAX_VALUE);

    }


    public static void delete(CuratorFramework curator, String path) throws Exception {
        Stat stat = curator.checkExists().forPath(path);
        if(stat != null){
            System.out.println(stat.toString());
            curator.delete().deletingChildrenIfNeeded().forPath(path);

            //强制保证删除，由于网络问题，导致删除失败，这样会重试知道删除为止
//            curator.delete().guaranteed().forPath("/path");
        }
    }
}
