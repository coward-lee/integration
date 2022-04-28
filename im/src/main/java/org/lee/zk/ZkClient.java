package org.lee.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.w3c.dom.Node;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class ZkClient {
    private final Logger log = LogManager.getLogger(ZkClient.class);
    String conStr = "42.192.85.33:2181";

    public static ZkClient getZkClient() {
        try {
            if (zkClient == null) zkClient = new ZkClient();
            return zkClient;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ZkClient zkClient;


    private static CuratorFramework client;
    String basePath = "/lee";


    public ZkClient() throws IOException {
        ExponentialBackoffRetry exponentialBackoffRetry = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(conStr, exponentialBackoffRetry);
        client.start();
    }

    public void createBase() {
        try {
            if (client.checkExists().forPath(basePath) == null) {
                client.create().forPath(basePath);
            }
            log.info("创建zookeeper基础节点成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createNode(String nodeName) {
        try {
            String nodePath = getNodePath(nodeName);
            if (client.checkExists().forPath(nodePath) == null) {
                client.create().forPath(nodePath);
                log.info("创建节点成功");
            }else{
                log.info("节点已经创建");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<String> getAllNode() {
        try {
            return client.getChildren().forPath(basePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public String getData(String nodeName) {
        try {
            byte[] bytes = client.getData().forPath(getNodePath(nodeName));
            return new String(bytes, 0, bytes.length, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setData(String nodeName, Integer data){
        try {
            client.setData().forPath(getNodePath(nodeName), (data+"").getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getNodePath(String nodeName){
        return basePath+"/"+nodeName;
    }

}
