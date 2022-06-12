package org.lee.study.type;

import java.util.Arrays;

public class MultiObjects {
    public static void main(String[] args) {
        Pair<String> demo = new Pair<>();
        demo.setVal("xxxxx");
        Pair<Integer> demo2 = new Pair<>();
        demo2.setVal(1111);
        System.out.println(demo.getVal().getClass() +"     "+demo2.getVal().getClass());
        Arrays.stream(demo.getClass().getDeclaredFields()).forEach(field -> System.out.print(field.getType() +"   "));
        System.out.println();
        Arrays.stream(demo2.getClass().getDeclaredFields()).forEach(field -> System.out.print(field.getType() +"   "));
//        demo.getClass().getClass
    }
}
