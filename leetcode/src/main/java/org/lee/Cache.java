package org.lee;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Cache {
    public static class Node {
        private Node next;
        private Node pre;
        private Object data;


        public void moveBack(Node node) {
            Node hNext = node.next;
            node.next = this;
            this.pre = node;
            this.next = hNext;
            if (hNext != null) { // in case npe
                hNext.pre = this;
            }
        }
    }

    private Map<String, Node> store = new HashMap<>();
    private Node head = new Node();

    public Object get(String key, Function<String, Object> loader) {
        Node node = store.get(key);
        if (node == null) {
            Object apply = loader.apply(key);
            node = new Node();
            node.data = apply;
        }

        moveToHead(node);
        return node.data;
    }

    private void moveToHead(Node node) {
        node.moveBack(head);
    }

    public static void main(String[] args) {
        Cache cache = new Cache();
        System.out.println(cache.get("aa", k -> k));
        System.out.println(cache.get("v",k->k));
        System.out.println(cache.get("c",k->k));
        System.out.println(cache.get("d",k->k));
        System.out.println(cache.get("d",k->k));
        System.out.println(cache.get("e",k->k));
    }
}
