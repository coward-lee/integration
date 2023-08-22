package org.lee.study.util;

import java.time.LocalDateTime;

public class PrintUtil {

    public static void print(String out) {
        System.out.println(LocalDateTime.now() + " - " + Thread.currentThread().getName() + ": " + out);
    }
}
