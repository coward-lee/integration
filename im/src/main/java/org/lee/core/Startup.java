package org.lee.core;

import org.lee.zk.ZkClient;

public class Startup {
    String name1 = "node1";
    static ZkClient zkClient = ZkClient.getZkClient();

    public static void start(String nodeName, Integer data) {
        zkClient.createBase();
        zkClient.createNode(nodeName);
        zkClient.setData(nodeName, data);
        ServerContainer.setServerSeq(data == 80 ? 0 : 1);
    }
}
