package org.lee.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class TestCurator {

    String conStr = "42.192.85.33:2181";
    CuratorFramework client;
    String path = "/lee";
    @BeforeEach
    void test_create(){
        ExponentialBackoffRetry exponentialBackoffRetry = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(conStr, exponentialBackoffRetry);
        client.start();

    }

    @Test
    void test_create_node() throws Exception {


        String data="hello";
        String s = client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(path, data.getBytes(StandardCharsets.UTF_8));
        System.out.println("结果 ： "+s);
    }


    @Test
    void test_read() throws Exception {
        client.getChildren().forPath(path).forEach(System.out::println);
        byte[] bytes = client.getData().forPath(path);
        System.out.println(new String(bytes,0,bytes.length, StandardCharsets.UTF_8));
    }

    @Test
    void test_update() throws Exception {
        Stat stat = client.setData().forPath(path, "xxxx".getBytes(StandardCharsets.UTF_8));
        System.out.println(stat.getAversion());
    }

}
