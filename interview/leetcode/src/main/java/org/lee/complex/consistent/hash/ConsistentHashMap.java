package org.lee.complex.consistent.hash;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ConsistentHashMap {
    int capacity = Integer.MAX_VALUE;
    List<Node> nodes = new LinkedList<>();

    public Data put(Data value) {
        Node current = nodes.get(0);
        for (int i = 1; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (value.getKeyIndex() < node.getIndex()) {
                break;
            } else {
                current = node;
            }
        }
        current.addData(value);
        System.out.println("PUT NODE IS " + current.getName());
        return value;
    }

    public void removeNode(Node key) {
        int i = nodes.indexOf(key);
        Node node = nodes.get(i == 0 ? nodes.size() - 1 : i - 1);
        node.getData().addAll(key.getData());
    }

    public void addNode(Node key) {
        if (nodes.size() == 0) {
            nodes.add(key);
            return;
        }

        Integer index = key.getIndex();

        Node current = nodes.get(0);
        for (int i = 1; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (index < node.getIndex()) {
                break;
            } else {
                current = node;
            }
        }
        Iterator<Data> iterator = current.getData().iterator();
        while (iterator.hasNext()) {
            Data next = iterator.next();
            if (next.getKeyIndex() < key.getIndex()) {
                key.addData(next);
                break;
            }
        }
        while (iterator.hasNext()) {
            Data next = iterator.next();
            key.addData(next);
        }
        nodes.add(key);
    }

    public int getSeg() {
        return capacity * nodes.size();
    }

    public int getIndex(int code) {
        return code / getSeg();
    }
}
