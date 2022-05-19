package org.lee.study.reactor.custom;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class Consumer<T> implements Subscriber<T> {
    Fun<T> fun;
    public Consumer(Fun<T> fun) {
        this.fun = fun;
    }

    @Override
    public void onSubscribe(Subscription s) {
        System.out.println("消费订阅行为");
    }

    @Override
    public void onNext(T s) {
        fun.fun(s);
    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }
}
