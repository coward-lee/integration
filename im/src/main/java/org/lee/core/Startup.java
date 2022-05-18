package org.lee.core;

import org.lee.zk.ZkClient;

import java.util.HashMap;
import java.util.Map;

import static org.lee.core.Config.port0;
import static org.lee.core.Config.port1;

public class Startup {

    static Map<Integer, Integer> portToSeq = new HashMap<>();
    static {
        portToSeq.put(port0, 0);
        portToSeq.put(port1, 1);
    }

    public static void start(String nodeName, Integer port) {
        ServerContainer.setServerSeq(portToSeq.get(port));
    }
}
