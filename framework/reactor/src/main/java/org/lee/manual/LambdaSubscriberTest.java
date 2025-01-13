package org.lee.manual;

import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;

import java.util.function.Consumer;

public class LambdaSubscriberTest<T> implements CoreSubscriber<T> {
    Consumer<? super Object> consumer;


    LambdaSubscriberTest(Consumer<? super Object> consumer) {
        this.consumer = consumer;
    }
    @Override
    public void onSubscribe(Subscription s) {

    }

    @Override
    public void onNext(T t) {
        if (consumer!=null){
            consumer.accept(t);
        }
    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }
}
