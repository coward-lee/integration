package org.lee.study.reactor.custom;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Producer<T> implements Publisher<T>, Subscription {

    Queue<T> queue = new LinkedList<>();
    Subscriber<? super T> subscriber;

    @Override
    public void subscribe(Subscriber<? super T> s) {
        System.out.println("publisher 被订阅了");
        this.subscriber = s;
    }

    public void subscribe(Fun<T> fun) {
        Consumer<T> consumer = new Consumer<T>(fun);
        subscribe(consumer);
        while (!queue.isEmpty()){
            request(1);
        }
    }

    @Override
    public void request(long n) {
        for (int i = 0; i < n; i++) {
            T poll = queue.poll();
            assert poll != null;
            subscriber.onNext(poll);
        }
    }

    @Override
    public void cancel() {
        System.out.println("cancel");
    }

    public static <T> Producer<T> create(Iterable<T> iterable){
        Producer<T> producer = new Producer<>();
        Queue<T> queue = producer.queue;
        iterable.forEach(queue::offer);
        return producer;
    }


    public static void main(String[] args) {
        Producer<String> producer = Producer.create(List.of("111", "222","333"));
        producer.subscribe(System.out::println);
    }

}

@FunctionalInterface
interface Fun<T>{
    void fun(T str);
}
