package org.lee.study.reactor;

import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.ParallelFlux;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Basic {

    @Test
    public void main() {
        List<String> strings = IntStream.range(1, 10).boxed().map(d -> d + "").collect(Collectors.toList());

        Subscription subscription = new Subscription() {
            @Override
            public void request(long n) {
                strings.get((int) n);
            }

            @Override
            public void cancel() {

            }
        };

        Publisher<String> publisher = new Publisher<>() {
            @Override
            public void subscribe(Subscriber<? super String> s) {
                System.out.println("publisher#subscribe");
                s.onSubscribe(subscription);
            }
        };
        Subscriber<String> subscriber = new Subscriber<>() {
            Subscription sub;

            @Override
            public void onSubscribe(Subscription s) {
                this.sub = s;
            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };


        publisher.subscribe(subscriber);

    }

    /**
     * generate这个哥们生成的数据是同步生成的
     */
    @Test
    public void test_back_press_generate() throws InterruptedException {
//        List<String> strings = IntStream.range(1, 10).boxed().map(d -> d + "").collect(Collectors.toList());

        AtomicInteger atomicInteger = new AtomicInteger(0);
        System.out.println(LocalDateTime.now());
        ParallelFlux<Object> generate = Flux.generate((sink) -> {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String s = atomicInteger.getAndIncrement() + "";
                    System.out.println("publisher:" + s);
                    if (s.equalsIgnoreCase("100")) {
                        sink.complete();
                    }
                    sink.next(s);
                }).onBackpressureBuffer(10240)
                .parallel(16);
//                .delayElements(Duration.ofMillis(5000));
        generate.subscribe((o) -> {
            try {

                System.out.print(LocalDateTime.now() + ":");
                System.out.println(o);
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * create 这个哥们生成的数据是同步生成的
     */
    @Test
    public void test_back_press_create() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        System.out.println(LocalDateTime.now());
        Flux<Object> flux = Flux.create(sink -> {

            while (true) {
                try {
                    executorService.submit(() -> {
                        try {
                            String s = atomicInteger.getAndIncrement() + "";
                            System.out.println("publisher:" + s);
                            sink.next(s);
                            Thread.sleep(10);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, FluxSink.OverflowStrategy.ERROR);
//                .onBackpressureBuffer(20);

        flux.subscribe(new Subscriber<Object>() {
            Subscription subscription ;
            @Override
            public void onSubscribe(Subscription s) {
                this.subscription = s;
                s.request(1);
            }

            @Override
            public void onNext(Object o) {

            try {
                System.out.print(LocalDateTime.now() + ":");
                System.out.println(o);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                subscription.request(1);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
//        flux.subscribe((o) -> {
//            try {
//                System.out.print(LocalDateTime.now() + ":");
//                System.out.println(o);
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        Flux<Object> generate1 = Flux.generate((sink) -> strings.get(atomicInteger.get()));
        Thread.sleep(Integer.MAX_VALUE);
    }

}
