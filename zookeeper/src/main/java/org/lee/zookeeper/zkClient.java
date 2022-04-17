package org.lee.zookeeper;

import org.apache.zookeeper.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class zkClient {


    String conStr = "42.192.85.33:2181";
    ZooKeeper zkClient;
    private final Logger log = LoggerFactory.getLogger(zkClient.class);
    CountDownLatch connectSignal = new CountDownLatch(1);


    // 链接服务器
    @BeforeEach
    public void test_connect() throws IOException {
        zkClient = new ZooKeeper(conStr, 2000, event ->
        {
            System.out.println("连接成功");
            connectSignal.countDown();
        }
        );
        System.out.println("链接成功了吗?");
    }

    // 创建客户端
    @Test
    public void test_create() throws Exception {
        connectSignal.await();
        String s = zkClient.create(
                "/lee/lee", //  路径
                "lee.avi".getBytes(StandardCharsets.UTF_8), // 内容
                ZooDefs.Ids.OPEN_ACL_UNSAFE, // 内容的权限控制
                CreateMode.PERSISTENT   // 模式
        );
        System.out.println("创建成功的内容返回：{}"+s);
    }

    /**
     *     获取子节点
     */
    @Test
    void test_getChildren() throws Exception {
        connectSignal.await();
        List<String> children = zkClient.getChildren("/", true);
        children.forEach(System.out::println);
    }




}
