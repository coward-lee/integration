package org.lee.guava.utilities;

import com.google.common.cache.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ExecutionException;

public class GuavaCacheTest {
    public static void main(String[] args) throws Throwable {
        new GuavaCacheTest().test();
    }
    @Test
    void test() throws Throwable {
        Cache<Object, Object> build = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofSeconds(1000000))
                .maximumSize(2)
//                .refreshAfterWrite(Duration.ofSeconds(1000))
                .removalListener(notification -> System.out.println(notification.getKey()+":"+notification.getValue()+"was removed , calused by "+ notification.getCause().name()))
//                .build(CacheLoader.from(() -> {
//                    System.out.println("loader invoked");
//                    return "";
//                }));
                .build();
        String k = "k";
        build.get(k, () -> load());
        build.get(k+"1", () -> load());
//        Thread.sleep(2000);
//        build.get(k, () -> load());
        new Thread(()->{
            while (true){
                try {
                    int read = System.in.read();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    System.out.println("load");
                    build.get(k+"xxx", () -> load());
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        Thread.sleep(Integer.MAX_VALUE);
    }
    String load(){
        System.out.println("loader invoked");
        return "value";
    }
}
