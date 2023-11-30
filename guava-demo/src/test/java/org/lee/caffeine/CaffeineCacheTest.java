package org.lee.caffeine;

import com.github.benmanes.caffeine.cache.*;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class CaffeineCacheTest {


    /**
     * 所有使用特性
     */

    @Test
    void test_all() {
        Cache<Object, Object> cache = Caffeine.newBuilder()
                .weakKeys() // 弱引用key
                .weakValues()// 弱引用 value
                .executor(Executors.newCachedThreadPool()) // 自定义加载线程池 ForkJoinPool.commonPool()， 默认使用，the executor to use for asynchronous execution
                .recordStats() // 数量统计
                .maximumSize(10_000) // 根据数量淘汰策略
                .expireAfter(new MyExpiry())
                .scheduler(Scheduler.disabledScheduler()) // 定期清空数据的一个机制，可以不设置，如果不设置则不会主动的清空过期数据；
                .build();

        Policy<Object, Object> policy = cache.policy();// 策略????
        cache.policy().eviction().ifPresent(c -> c.setMaximum(2 * c.getMaximum())); // 动态修改最大缓存数量阈值
        cache.policy().expireAfterAccess().ifPresent(expiration -> {}); // 运行时动态修改过期的阈值
        cache.policy().expireAfterWrite().ifPresent(expiration -> {}); // 运行时动态修改过期的阈值
        cache.policy().expireVariably().ifPresent(expiration -> {}); // 运行时动态修改过期的阈值
        cache.policy().refreshAfterWrite().ifPresent(expiration -> {}); // 运行时动态修改过期的阈值

//        cache.f

    }


    class MyExpiry implements Expiry<Object,Object> {
        @Override
        public long expireAfterCreate(Object key, Object value, long currentTime) {
            return 0;
        }

        @Override
        public long expireAfterUpdate(Object key, Object value, long currentTime, @NonNegative long currentDuration) {
            return 0;
        }

        @Override
        public long expireAfterRead(Object key, Object value, long currentTime, @NonNegative long currentDuration) {
            return 0;
        }
    };

    /**
     * 同步加载版本
     *
     * @throws InterruptedException
     */
    @Test
    void demo_test() throws InterruptedException {
        int size = 100000;
        Cache<Object, Object> build = Caffeine.newBuilder()
                .executor(ForkJoinPool.commonPool())
                .maximumSize(100000)
                .removalListener((k, v, c) -> {
                    System.out.println(k + ":" + v + ":" + c);
                    System.out.println(k + ":" + v + ":" + c);
                })
                .build(key -> key + "value");

        for (int i = 0; i <100000 ; i++) {
            build.get("x"+i, (a) -> "x");
            build.get("x"+i, (a) -> "x");
            build.get("2x"+i, (a) -> "x");
            build.get("3x"+i, (a) -> "x");
            build.get("1x"+i, (a) -> "x");
            build.get("x"+i, (a) -> "x");
            build.get("1x"+i, (a) -> "x");
            build.get("x"+i, (a) -> "x");
        }
    }


    /**
     * 异步加载
     *
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
        build.get("x", (a) -> "x");
        build.get("2x", (a) -> "x");
        build.get("3x", (a) -> "x");
        Thread.sleep(1000 * 2);
        build.get("1x", (a) -> "x");
    }

    /**
     * TimerWheel ,用于在不同过期时间的key，不通过的key放到不同的 timer Wheel 之上，以减少timer wheel的step次数
     * 动态指定过期时间
     *
     * @throws InterruptedException
     */
//    @Test
    void demo_test_TimerWheel() throws InterruptedException {
        AsyncCache<Object, Object> build = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofSeconds(1))
                .removalListener((k, v, c) -> System.out.println(k + ":" + v + ":" + c))
                /**
                 * 动态指定过期时间
                 */
                .expireAfter(new Expiry<>() {
                    //返回创建后的过期时间
                    @Override
                    public long expireAfterCreate(@NonNull Object key, @NonNull Object value, long currentTime) {
                        return 0;
                    }

                    //返回更新后的过期时间
                    @Override
                    public long expireAfterUpdate(@NonNull Object key, @NonNull Object value, long currentTime, @NonNegative long currentDuration) {
                        return 0;
                    }

                    //返回更新后的过期时间
                    @Override
                    public long expireAfterRead(@NonNull Object key, @NonNull Object value, long currentTime, @NonNegative long currentDuration) {
                        return 0;
                    }
                })
                .recordStats() // 记录当前状态
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

    @Test
    void test_graph() {
        Cache<Object, Object> build = Caffeine.newBuilder()
                .weakKeys()
                .weakValues()
                .build();
//        LoadingCache<Key, Graph> graphs = Caffeine.newBuilder()
//                .weakKeys()
//                .weakValues()
//                .build(key -> createExpensiveGraph(key));
    }
}
