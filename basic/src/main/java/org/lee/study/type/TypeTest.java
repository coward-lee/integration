package org.lee.study.type;

import java.util.ArrayList;

public class TypeTest {
    public static void main(String[] args) {
        ArrayList strings = new ArrayList<String>();
        strings.add(123);
        Object o = strings.get(0);
        System.out.println(o);
    }
}
