package org.lee.study.feature;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FilterOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JDK9 {
    List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

    @Test
    public void test_copy_of() {
        byte[] bytes = new byte[1 << 10];
//        try (this.getClass().getResourceAsStream("/demo"){}.finalWrapper.finalCloseable){
//
//        }
    }

    /**
     * 匿名类内部类，支持泛型的 <> 这个符号
     */
    @Test
    void test_diamond() {
        FooClass<Integer> fc = new FooClass<>(1) { // anonymous inner class
        };

        FooClass<? extends Integer> fc0 = new FooClass<>(1) {
            // anonymous inner class
        };

        FooClass<?> fc1 = new FooClass<>(1) { // anonymous inner class
        };
    }

    /**
     * 接口的私有方法，用来防止default方法代码过长
     */
    interface InterfaceWithPrivateMethods {

        private static String staticPrivate() {
            return "static private";
        }

        private String instancePrivate() {
            return "instance private";
        }

        default void check() {
            String result = staticPrivate();
            InterfaceWithPrivateMethods pvt = new InterfaceWithPrivateMethods() {
                // anonymous class
            };
            result = pvt.instancePrivate();
        }
    }
}


class FooClass<T> {
    public FooClass(T i) {
    }
}
