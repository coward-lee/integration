package org.lee.manual;

import reactor.core.CoreSubscriber;

import java.util.Arrays;
import java.util.List;

public class FluxJustTest<T> extends FluxTest<T>{
     T value;
     List<T> values;

    public FluxJustTest(T[] data) {
        values = Arrays.stream(data).toList();
    }

    @Override
    public void subscribe(CoreSubscriber<? super T> subscriber) {
        subscriber.onNext(value);
    }
}
