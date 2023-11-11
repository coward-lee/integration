package org.lee.guava.utilities;

import com.github.benmanes.caffeine.cache.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

public class CaffeineCacheTest {

    /**
     * 同步加载版本
     * @throws InterruptedException
     */
    @Test
    void demo_test() throws InterruptedException {
        Cache<Object, Object> build = Caffeine.newBuilder()
                .executor(ForkJoinPool.commonPool())
                .maximumSize(1)
                .removalListener((k, v, c) -> {
                    System.out.println(k + ":" + v + ":" + c);
                    System.out.println(k + ":" + v + ":" + c);
                })
                .build(key-> key + "value");

        build.get("x", (a) -> "x");
        build.get("1x", (a) -> "x");
        build.get("2x", (a) -> "x");
        build.get("3x", (a) -> "x");
        Thread.sleep(1000 * 2);
        build.get("1x", (a) -> "x");
    }


    /**
     * 异步加载
     * @throws InterruptedException
     */
    @Test
    void demo_test_with_async() throws InterruptedException {
        AsyncCache<Object, Object> build = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofSeconds(1))
                .removalListener((k, v, c) -> System.out.println(k + ":" + v + ":" + c))
                .buildAsync(new AsyncCacheLoader<>() {
                    @Override
                    public @NonNull CompletableFuture<Object> asyncLoad(@NonNull Object key, @NonNull Executor executor) {
                        return CompletableFuture.supplyAsync(() -> key + "async loading");
                    }
                });

        build.get("x", (a) -> "x");
        build.get("1x", (a) -> "x");
        build.get("2x", (a) -> "x");
        build.get("3x", (a) -> "x");
        Thread.sleep(1000 * 2);
        build.get("1x", (a) -> "x");
    }

    @Test
    void test_compute_if_present() {
        HashMap<Object, Object> map = new HashMap<>();
        map.putIfAbsent("1", "s");
        map.putIfAbsent("2", "s");
        map.computeIfPresent("1", (k, v) -> null);
    }
}
