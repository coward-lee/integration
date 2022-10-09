package org.lee.manual;

import org.reactivestreams.Subscriber;
import reactor.core.CorePublisher;
import reactor.core.CoreSubscriber;

public abstract class FluxTest<T> implements CorePublisher<T> {

    @Override
    public void subscribe(Subscriber<? super T> s) {
        System.out.println("FluxTest.subscribe");
    }

    public final <E extends Subscriber<? super T>> E subscribeWith(E subscriber){
        subscribe(subscriber);
        return subscriber;
    }
    @SafeVarargs
    public static <R> FluxTest<R> just(R... data){
        return new FluxJustTest(data);
    }
}
