package org.lee.study.loader;

import java.sql.Driver;
import java.util.ServiceLoader;

public class SpiDemo {
    public static void main(String[] args) {
        ServiceLoader<Driver> load = ServiceLoader.load(Driver.class);
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(load);
        System.out.println(contextClassLoader);
    }
}
