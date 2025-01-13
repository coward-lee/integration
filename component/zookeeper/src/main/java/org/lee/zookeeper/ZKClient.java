package org.lee.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;

class ZKClient {


    String conStr = "42.192.85.33:2181";
    ZooKeeper zkClient;
    CountDownLatch connectSignal = new CountDownLatch(1);


    // 链接服务器
    @BeforeEach
    public void test_connect() throws IOException {
        //  节点监听，监听的那个一个节点或者监听的哪一个路径
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
        System.out.println("创建成功的内容返回：{}" + s);
    }

    /**
     * 获取子节点
     */
    @Test
    void test_getChildren() throws Exception {
        connectSignal.await();
        List<String> children = zkClient.getChildren("/", true);
        children.forEach(System.out::println);
    }

    /**
     * 节点监听
     */
    @Test
    void test_watch() throws InterruptedException {

        connectSignal.await();
        try {

            List<String> children = zkClient.getChildren("/", true);
            System.out.println("================================================================================================");
            children.forEach(System.out::println);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 判断节点是否存在
     */
    @Test
    void test_exist() throws InterruptedException, KeeperException {
        Stat exists = zkClient.exists("/lee10", false);
        System.out.println(exists);
    }


    /**
     * 获取servers的节点
     */
    @Test
    void test_server_watch() throws InterruptedException, KeeperException {
        zkClient.getChildren("/servers/", event -> {
            try {
                String root = "/servers/";
                List<String> children = zkClient.getChildren(root, true);
                System.out.println("================================================================================================");
                children.forEach(server -> {
                    try {
                        zkClient.getData(root + server, false, null);
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 更新节点
     */
    @Test
    void test_Set() throws InterruptedException, KeeperException {
        connectSignal.await();
//        zkClien
        Stat stat = zkClient.setData("/lee/lee", "xxxx".getBytes(StandardCharsets.UTF_8), -1);
        System.out.println("stat:" + stat.getAversion());
        stat = zkClient.setData("/lee/lee", "asdx".getBytes(StandardCharsets.UTF_8), -1);

        zkClient.getChildren("/lee/lee", false).forEach(System.out::println);
    }

    @Test
    void test_set(){

    }

}
