package org.lee.study.reactor.aysnc;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Producer<T> implements Publisher<T>, Subscription {

    BlockingQueue<T> queue = new ArrayBlockingQueue<>(100);
    Subscriber<? super T> subscriber;

    @Override
    public void subscribe(Subscriber<? super T> s) {
        System.out.println("publisher 被订阅了");
        this.subscriber = s;
    }

    public void subscribe(Fun<T> fun) {

        Consumer<T> consumer = new Consumer<T>(fun);
        subscribe(consumer);
        Thread t = new Thread(() -> {
            while (true) {
                request(1);
            }
        });
        t.start();
    }

    @Override
    public void request(long n) {
        for (int i = 0; i < n; i++) {
            try {
                T poll = queue.take();
                assert poll != null;
                subscriber.onNext(poll);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void cancel() {
        System.out.println("cancel");
    }

    private Thread thread;

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Thread getThread() {
        return thread;
    }

    public static <T> Producer<T> create(Iterable<T> iterable) {
        Producer<T> producer = new Producer<>();
        BlockingQueue<T> queue = producer.queue;

        producer.setThread(new Thread(() -> iterable.forEach(item->{
            try {
                System.out.println(item);
                queue.put(item);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        })));
        producer.getThread().start();

        return producer;
    }


    public static void main(String[] args) {
        List<Integer> collect = IntStream.range(1, 200).boxed().collect(Collectors.toList());

        Producer<Integer> producer = Producer.create(collect);
        producer.subscribe((content) ->{
            Thread.sleep(100);
            System.out.println("客户端："+content);
        });
    }

}

@FunctionalInterface
interface Fun<T> {
    void fun(T str) throws InterruptedException;
}
