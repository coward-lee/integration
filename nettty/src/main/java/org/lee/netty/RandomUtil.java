package org.lee.netty;

import java.util.Random;

public class RandomUtil {
    public static Integer get(int limit){
        Random random = new Random();
        int i  = random.nextInt();
        i = Math.abs(i);
        return i%limit;
    }
}
