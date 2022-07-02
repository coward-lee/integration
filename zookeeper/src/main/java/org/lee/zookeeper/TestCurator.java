package org.lee.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class TestCurator {

    String conStr = "42.192.85.33:2181";
    CuratorFramework client;
    String path = "/lee";

    @BeforeEach
    void test_create() {
        ExponentialBackoffRetry exponentialBackoffRetry = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(conStr, exponentialBackoffRetry);
        client.start();

    }

    @Test
    void test_create_node() throws Exception {


        String data = "hello";
        String s = client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(path, data.getBytes(StandardCharsets.UTF_8));
        System.out.println("结果 ： " + s);
    }


    @Test
    void test_read() throws Exception {
        client.getChildren().forPath(path).forEach(System.out::println);
        byte[] bytes = client.getData().forPath(path);
        System.out.println(new String(bytes, 0, bytes.length, StandardCharsets.UTF_8));
    }

    @Test
    void test_update() throws Exception {
        Stat stat = client.setData().forPath(path, "xxxx".getBytes(StandardCharsets.UTF_8));
        System.out.println(stat.getAversion());
    }

    @Test
    void test_delete() throws Exception {
        client.delete().forPath("/lee/81");

        client.getChildren().forPath("/lee").forEach(System.out::println);
    }


    /**
     * watcher 不适合节点频繁变化
     */
    @Test
    void test_watch() throws Exception {
        // 原始api使用监听器
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent event) {

                System.out.println("监听器" + event);
                try {
                    byte[] bytes = client.getData().usingWatcher(this).forPath("/lee");
                    System.out.println(new String(bytes, 0, bytes.length, StandardCharsets.UTF_8));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        byte[] bytes = client.getData().usingWatcher(watcher).forPath("/lee");

        // 使用Curator
        System.out.println(new String(bytes, 0, bytes.length, StandardCharsets.UTF_8));
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    void test_nodeCache() throws Exception {
        NodeCache nodeCache = new NodeCache(client, "/lee", false);
        NodeCacheListener nodeCacheListener = new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                ChildData currentData = nodeCache.getCurrentData();
                byte[] bytes = currentData.getData();
                System.out.println(new String(bytes, 0, bytes.length, StandardCharsets.UTF_8));
            }
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start();
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    void test_CuratorCache() throws Exception {
        CuratorCache curatorCache = CuratorCache.builder(client, "/lee").build();

        CuratorCacheListener curatorCacheListener = (type, oldData, data) -> {
            byte[] bytes = oldData.getData();
            System.out.println("oldData:" + new String(bytes, 0, bytes.length, StandardCharsets.UTF_8));

            byte[] data1 = data.getData();
            System.out.println("data:" + new String(data1, 0, data1.length, StandardCharsets.UTF_8));
        };

        curatorCache.listenable().addListener(curatorCacheListener);
        curatorCache.start();
        Thread.sleep(Integer.MAX_VALUE);
    }

}
