package com.huang.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.EnsurePath;

/**
 * Description:
 * Created on 2018/3/1 17:27
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public class CuratorMasterSelect {

    public static void main(String[] args) throws InterruptedException {
        CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString("localhost:2291")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(3000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        curator.start();
        String masterPath = "/curator_master_path";

        LeaderSelector selector = new LeaderSelector(curator, masterPath, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                System.out.println("成为master");
                //一旦执行完takeLeadership，curator就会立即执行释放Master的权利，重新开始一轮master的选举
                Thread.sleep(3000);
                System.out.println("完成master操作，释放Master 权利");
            }
        });

        selector.autoRequeue();
        selector.start();

        Thread.sleep(Integer.MAX_VALUE);
    }
}
