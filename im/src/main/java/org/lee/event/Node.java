package org.lee.event;

public class Node {

    private String nodeName;
    private Integer nodeKey;

    public Node(String nodeName, Integer nodeKey) {
        this.nodeName = nodeName;
        this.nodeKey = nodeKey;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Integer getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(Integer nodeKey) {
        this.nodeKey = nodeKey;
    }
}
