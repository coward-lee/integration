package org.lee.study.functional;

import java.util.*;
import java.util.function.Supplier;

/**
 * 函数式编程
 */
public class FunctionalDemo {

    public static void main(String[] args) {
        new FunctionalDemo().testSupplier(()->Arrays.asList("args", "sss", "xxx", "ddd"));
        System.out.println(ViewDemo.instance(()->Arrays.asList("args", "sss", "xxx", "ddd")));
    }

    public void testSupplier(Supplier<List<String>> supplier){
        supplier.get().forEach(System.out::println);
    }

    static class ViewDemo{
        Integer size;
        String first;

        List<String> list = new ArrayList<>();
        public static ViewDemo instance(Supplier<List<String>> listFun){
            ViewDemo viewDemo = new ViewDemo();
            List<String> strings = listFun.get();
            strings.stream().findFirst().ifPresent(s -> viewDemo.first = s);
            viewDemo.size = strings.size();
            viewDemo.list = strings;
            return viewDemo;
        }

        @Override
        public String toString() {
            return "ViewDemo{" +
                    "size=" + size +
                    ", first='" + first + '\'' +
                    ", list=" + list +
                    '}';
        }
    }
}
