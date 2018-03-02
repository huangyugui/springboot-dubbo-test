package com.huang.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * Description:
 * Created on 2018/3/1 16:55
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class CuratorNodeCacheSample {

    public static void main(String[] args) throws Exception {
        CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString("localhost:2291")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(3000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        curator.start();

        delete(curator, "/a");

        curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/a/b", "/a/b".getBytes());

//        NodeCache cache = new NodeCache(curator, "/a/c", false);
//        cache.start(true);
//        cache.getListenable().addListener(new NodeCacheListener() {
//            @Override
//            public void nodeChanged() throws Exception {
//                if(cache.getCurrentData() != null){
//                    System.out.println("node data update, new data: =========" + new String(cache.getCurrentData().getData()));
//                    System.out.println("stat: ========"+ cache.getCurrentData().getStat());
//                }else{
//                    System.out.println("节点被删除了");
//                }
//            }
//        });

        //对子节点进行监听，但是无法对二级子节点进行监听，例如/a/b/c创建时就不能监听
        PathChildrenCache pcache = new PathChildrenCache(curator, "/a", true);
        pcache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        pcache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                System.out.println(event.getType() + "------" + event.getData());
            }
        });

        curator.create().forPath("/a/c", "/a/c".getBytes());
        Thread.sleep(1000);
        curator.delete().forPath("/a/c");
        curator.setData().forPath("/a/b", "/a/b udpate".getBytes());
        curator.setData().forPath("/a", "/a udpate".getBytes());
        curator.getData().forPath("/a/b");
    }

    public static void delete(CuratorFramework curator, String path) throws Exception {
        Stat stat = curator.checkExists().forPath(path);
        if(stat != null){
//            System.out.println(stat.toString());
            curator.delete().deletingChildrenIfNeeded().forPath(path);

            //强制保证删除，由于网络问题，导致删除失败，这样会重试知道删除为止
//            curator.delete().guaranteed().forPath("/path");
        }
    }
}
