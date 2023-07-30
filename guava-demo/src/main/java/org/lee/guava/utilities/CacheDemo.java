package org.lee.guava.utilities;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

public class CacheDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Cache<Object, Object> build = CacheBuilder.newBuilder()
                .expireAfterAccess(Duration.ofHours(1L))
                .refreshAfterWrite(Duration.ofMillis(1))
                .build(CacheLoader.from(()->""));
        String k = "k";
        build.get(k, () -> "v");
        build.get(k+"1", () -> "v");
        build.get(k, () -> "v");
        Thread.sleep(Integer.MAX_VALUE);
    }
}
