package org.lee.complex.consistent.hash;


import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 一致性hash 实现
 */
public class ConsistentHash {
    private List<Node> nodes = new LinkedList<>();
    static int size = 4;
    static int seg = Integer.MAX_VALUE / size;

    public static void main(String[] args) {
        ConsistentHash hash = new ConsistentHash();

        ConsistentHashMap map = new ConsistentHashMap();
        hash.initNode(map);

        List<Data> data = hash.initData("data", seg);
        data.forEach(map::put);
        data = hash.initData("data", 0);
        data.forEach(map::put);
    }

    public List<Data> initData(String seed, int initStart) {
        return IntStream.range(0 + initStart, 100 + initStart).mapToObj(c -> new Data(seed + c,c)).toList();
    }

    public void initNode(ConsistentHashMap map) {
        for (int j = 0, i = 0; i < size; j += seg, i++) {
            map.addNode(new Node(j, "node-" + i + "\t\t"));
        }
    }


}
