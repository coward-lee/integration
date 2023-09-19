package org.lee.guava.utilities;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.HashMap;

public class CaffeineCacheTest {

    @Test
    void demo_test() throws InterruptedException {
        Cache<Object, Object> build = Caffeine.newBuilder()
//                .maximumSize(2)
                .expireAfterAccess(Duration.ofSeconds(1))
                .removalListener((k, v, c) -> {
                    System.out.println(k.toString() + v + c);
                })
                .build();
        build.get("x", (a) -> "x");
        build.get("1x", (a) -> "x");
        build.get("2x", (a) -> "x");
        build.get("3x", (a) -> "x");
        Thread.sleep(1000 * 2);
        build.get("1x", (a) -> "x");
    }

    @Test
    void test_compute_if_present(){
        HashMap<Object, Object> map = new HashMap<>();
        map.putIfAbsent("1","s");
        map.putIfAbsent("2","s");
        map.computeIfPresent("1",(k,v)->{
            return null;
        });
    }
}
