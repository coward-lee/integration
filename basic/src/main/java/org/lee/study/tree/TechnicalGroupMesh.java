package org.lee.study.tree;


import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;

public class TechnicalGroupMesh implements Serializable {
    public TechnicalGroupMesh(String groupName) {
        this.groupName = groupName;
    }

    Long id;
    Long parentId;
    String parentName;
    String groupName;
    List<TechnicalGroupMesh> children;

    public void levelTraverse(){
        System.out.println();
        Queue<TechnicalGroupMesh> queue = new ArrayDeque<>();
        int level = 1;
        queue.add(this);
        int levelLen = queue.size();
        while (!queue.isEmpty()){
            System.out.println("当前层级："+level);
            for (int i = 0; i < levelLen; i++){
                TechnicalGroupMesh head = queue.poll();

                // todo
                System.out.print(head.groupName + ",");
                // todo

                if (head.children !=null && !head.children.isEmpty()) {
                    queue.addAll(head.children);
                }
            }
            levelLen = queue.size();
            level++;
            System.out.println();

        }
    }

    public static void main(String[] args) {
        TechnicalGroupMesh root = new TechnicalGroupMesh("root");
        ArrayList<TechnicalGroupMesh> children = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            children.add(new TechnicalGroupMesh("group"+i));
        }
        root.children = children;

        for (int i = 0; i < 3; i++) {
            children = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                children.add(new TechnicalGroupMesh("group"+i+"  "+j));
            }
            root.children.get(i).children = children;

        }
        root.levelTraverse();
    }
}
