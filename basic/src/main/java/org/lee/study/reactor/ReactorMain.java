package org.lee.study.reactor;


import org.junit.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.annotation.NonNull;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ReactorMain {
    /**
     * 创建 Flux和Mono
     */
    @Test
    public void createStream() {
        Flux<String> seq1 = Flux.just("foo", "bar", "foobar");
        seq1.toStream().forEach(System.out::print);
        System.out.println();
        List<String> iterable = Arrays.asList("foo", "bar", "foobar");
        iterable.stream().forEach(System.out::print);
        System.out.println();
        Flux<String> seq2 = Flux.fromIterable(iterable);
        seq2.toStream().forEach(System.out::print);
        System.out.println();

        Mono<String> noData = Mono.empty();
        String block = noData.block();
        System.out.println(block);
        Mono<String> data = Mono.just("foo");
        String block1 = data.block();
        System.out.println(block1);
        Flux<Integer> numbersFromFiveToSeven = Flux.range(5, 3);
        numbersFromFiveToSeven.toStream().forEach(System.out::print);
        System.out.println();
    }

    /**
     * 基于 lambda 的对 Flux 的订阅（subscribe）
     * subscribe();
     * <p>
     * subscribe(Consumer<? super T> consumer);
     * <p>
     * subscribe(Consumer<? super T> consumer, Consumer<? super Throwable> errorConsumer);
     * <p>
     * subscribe(Consumer<? super T> consumer,Consumer<? super Throwable> errorConsumer, Runnable completeConsumer);
     * <p>
     * subscribe(Consumer<? super T> consumer,Consumer<? super Throwable> errorConsumer, Runnable completeConsumer, Consumer<? super Subscription> subscriptionConsumer);
     */
    @Test
    public void subscribe() {
        Flux<Integer> ints = Flux.range(1, 3);
        ints.subscribe();
        System.out.println("=======================传入一个lambda=======================");
        ints.subscribe(System.out::println);
        System.out.println("=======================加入错误消费者=======================");
        Flux.range(1, 4)
                .map(i -> {
                    if (i <= 3) return i;
                    throw new RuntimeException("go to 4");
                }).subscribe(System.out::println, e -> System.out.println("错误发生了：" + e.getMessage()));
        System.out.println("=======================加入一个完成时候的消费者==========================");
        Flux.range(1, 4).subscribe(
                System.out::println,
                error -> System.err.println("Error " + error),
                () -> System.out.println("Done")
        );
        System.out.println("=======================加入一个，subscriber==========================");
        SampleSubscriber<Integer> ss = new SampleSubscriber<>();
        Flux<Integer> range = Flux.range(1, 100);
        range.subscribe(
                System.out::println,
                error -> System.err.println("Error " + error),
                () -> System.out.println("Done"),
                s -> {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ss.request(10);
                });
        range.subscribe(ss);
    }

    /**
     * 被压测试
     */
    @Test
    public void backPress() {
        Flux<Integer> range = Flux.range(1, 100);

        range.map(Math::sqrt).subscribe(new BaseSubscriber<Double>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(1);
            }

            @Override
            protected void hookOnNext(Double value) {
                try {
                    request(1);
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        range.subscribe(System.out::println);
    }

    /**
     * generate of flux 方法测试
     * <T, S> Flux<T> generate(Callable<S> stateSupplier, BiFunction<S, SynchronousSink<T>, S> generator)
     * <T, S> Flux<T> generate(Callable<S> stateSupplier, BiFunction<S, SynchronousSink<T>, S> generator, Consumer<? super S> stateConsumer)
     */
    @Test
    public void testGenerateInFlux() {
        // 直接生成不带生产者
        Flux<Object> generate = Flux.generate(() -> 0,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3 * state);
                    return state + 1;
                });
        // 需要在subscribe的时候指定消费者
        generate.subscribe(System.out::println);
        // generate 生成并附带消费者
        Flux<String> generateWithConsumer = Flux.generate(
                AtomicLong::new,
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next("3 x " + i + " = " + 3 * i);
                    if (i == 10) sink.complete();
                    return state;
                }, (state) -> System.out.println("state: " + state));
        // 可以直接subscribe
        generateWithConsumer.subscribe();
    }

    /**
     * create of flux 方法测试
     *
     * <T> Flux<T> create(Consumer<? super FluxSink<T>> emitter)
     */
    @Test
    public void create() {
        Flux<Object> objectFlux = Flux.create(sink -> {
            MyEventProcessor.register(new MyEventListener<String>() {
                @Override
                public void onDataChunk(List<String> chunk) {
                    chunk.forEach(sink::next);
                }

                @Override
                public void processComplete() {
                    sink.complete();
                }

                @Override
                public void onError(Throwable e) {
                    sink.error(e);
                }
            });
        });
        objectFlux.subscribe();
    }


    /**
     * handle method in flux of  flux or mono
     */

    @Test
    public void handle() {
        Flux<String> alphabet = Flux.just(-1, 30, 13, 9, 20)
                .handle((i, sink) -> {
                    String letter = alphabet(i);
                    if (letter != null)
                        sink.next(letter);
                });

        alphabet.subscribe(System.out::println);
    }

    public String alphabet(int letterNumber) {
        if (letterNumber < 1 || letterNumber > 26) {
            return null;
        }
        int letterIndexAscii = 'A' + letterNumber - 1;
        return "" + (char) letterIndexAscii;
    }

    /**
     * 调度器（Schedulers）
     */
    @Test
    public void schedulers() {

        // 当前线程
        Scheduler immediate = Schedulers.immediate();

        // 可重用单线程
        Scheduler single = Schedulers.single();
        Scheduler customSingle = Schedulers.newSingle("自定义名称");
        // 弹性线程池
        Scheduler elastic = Schedulers.boundedElastic();
        Scheduler newBoundedElastic = Schedulers.newBoundedElastic(1, 2, "自定义名称");
        // 固定大小线程池， 创建的线程池的大小与CPU个数等同
        Scheduler parallel = Schedulers.parallel();
        // 从线程池中创建Scheduler
        Scheduler from = Schedulers.fromExecutorService(Executors.newSingleThreadExecutor());

        Flux<Long> test = Flux.interval(Duration.ofMillis(300), Schedulers.newSingle("test"));
        test.subscribe(System.out::println);
    }

    /**
     * 线程模型测试
     * ?????
     * todo
     */
    @Test
    public void threadModel() throws InterruptedException {
        Flux<Integer> integerFlux = Flux.range(1, 10000)
                .publishOn(Schedulers.parallel());
        integerFlux.subscribe((s) -> {
            String name = Thread.currentThread().getName();
            System.out.println(name + ":" + s);
        });
        Thread.sleep(2000);
    }

    /**
     * 错误处理
     */
    @Test
    public void errorHandle() {
        Flux<Integer> map = Flux.range(1, 10)
                .map(x -> x * 2)
                .map(x -> {
                    if (x == 10) {
                        throw new RuntimeException("error happened.");
                    }
                    return x * x;
                });
        map.subscribe(
                value -> System.out.println("received:" + value),
                error -> System.err.println("error:" + error)
        );
        // 与下面的try-catch 相似
//        try {
//            for (int i = 1; i < 11; i++) {
//                String v1 = doSomethingDangerous(i);
//                String v2 = doSecondTransform(v1);
//                System.out.println("RECEIVED " + v2);
//            }
//        } catch (Throwable t) {
//            System.err.println("CAUGHT " + t);
//        }
    }

    /**
     * 静态缺省值
     * 这里也是发生错误后也会直接结束并返回默认值
     */
    @Test
    public void defaultValue() {
        Flux<Integer> integerFlux = Flux.just(10, 11, 12, 13).map(x -> {
            if (x % 2 == 1) {
                throw new RuntimeException("“xxxx");
            }
            return x;
        }).onErrorReturn(9999);
        integerFlux.subscribe(System.out::println);
    }

    /**
     * 异常处理方法
     * Flux.onErrorReturn
     * 如果predicate 返回true 返回 fallbackValue的值
     * 如果          返回为false 抛出错误
     * Flux<T> onErrorReturn(Predicate<? super Throwable> predicate, T fallbackValue)
     */
    @Test
    public void test_onErrorResume() {
//        Flux.just("key1", "key2").map(k -> {
//                    if (k.contains("2")) {
//                        throw new RuntimeException("发生了错误");
//                    }
//                    return k;
//                }).onErrorReturn((e) -> true, " 错误内容的返回")
//                .subscribe(System.out::println);
//        // 如果返回为false就报错
//        Flux.just("key1", "key2").map(k -> {
//                    if (k.contains("2")) {
//                        throw new RuntimeException("发生了错误");
//                    }
//                    return k;
//                }).onErrorReturn((e) -> false, " 错误内容的返回")
//                .subscribe(System.out::println);
//        Flux.just("timeout1", "unknown", "key2")
//                .flatMap(k -> {
//                    if (!k.contains("u")){
//                        throw new RuntimeException("xxxxxxxxx");
//                    }
//                    if (k.contains("k")){
//                        throw new Error("error");
//                    }
//                    return Flux.just(k.split("u"));
//                })
//                .onErrorResume(error -> {
//                    if (error instanceof TimeoutException)
//                        return Flux.just("error");
//                    else if (error instanceof RuntimeException)
//                        return  Flux.just("runtime exception");
//                    else
//                        return Flux.error(error);
//                }).subscribe(System.out::println);
        Mono<Object> xxxxxxx = Mono.create(System.out::println).onErrorResume(error -> Mono.just("xxxxxxx")).cache();
        xxxxxxx.subscribe(System.out::println);
    }
}

class SampleSubscriber<T> extends BaseSubscriber<T> {
    @Override
    public void hookOnSubscribe(@NonNull Subscription subscription) {
        System.out.println("subscribed");
        request(10);
    }

    @Override
    public void hookOnNext(@NonNull T value) {
        System.out.println(value);
        request(10);
    }
}
