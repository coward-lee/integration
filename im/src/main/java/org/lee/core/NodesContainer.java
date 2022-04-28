package org.lee.core;

import org.lee.event.Node;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodesContainer {
    public final static Map<Integer, Node> nodes = new ConcurrentHashMap<>(16);

    public static Node getNode(Integer key){
        return nodes.get(key);
    }

    public static void put(Integer key, Node node){
        nodes.put(key, node);
    }
}
