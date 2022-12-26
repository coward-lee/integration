package org.lee.study.stream;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

interface Sink<T> extends Consumer<T> {

    default void begin(long size) {

    }

    default void end() {

    }

    abstract class ChainedReference<T, OUT> implements Sink<T> {

        protected final Sink<OUT> downstream;

        public ChainedReference(Sink<OUT> downstream) {
            this.downstream = downstream;
        }
    }
}

@SuppressWarnings({"unchecked", "rawtypes"})
public class ReferenceChain<OUT, R> {

    /**
     * sink chain
     */
    private final List<Supplier<Sink<?>>> sinkBuilders = new ArrayList<>();

    /**
     * current sink
     */
    private final AtomicReference<Sink> sinkReference = new AtomicReference<>();

    public ReferenceChain<OUT, R> filter(Predicate<OUT> predicate) {
        //filter
        sinkBuilders.add(() -> {
            Sink<OUT> prevSink = (Sink<OUT>) sinkReference.get();
            Sink.ChainedReference<OUT, OUT> currentSink = new Sink.ChainedReference<>(prevSink) {

                @Override
                public void accept(OUT out) {
                    if (predicate.test(out)) {
                        downstream.accept(out);
                    }
                }
            };
            sinkReference.set(currentSink);
            return currentSink;
        });
        return this;
    }

    public ReferenceChain<OUT, R> map(Function<OUT, R> function) {
        // map
        sinkBuilders.add(() -> {
            Sink<R> prevSink = (Sink<R>) sinkReference.get();
            Sink.ChainedReference<OUT, R> currentSink = new Sink.ChainedReference<>(prevSink) {

                @Override
                public void accept(OUT in) {
                    downstream.accept(function.apply(in));
                }
            };
            sinkReference.set(currentSink);
            return currentSink;
        });
        return this;
    }

    public void forEachPrint(Collection<OUT> collection) {
        forEachPrint(collection, false);
    }

    public void forEachPrint(Collection<OUT> collection, boolean reverse) {
        Spliterator<OUT> spliterator = collection.spliterator();
        // 这个是类似于terminal op
        Sink<OUT> sink = System.out::println;
        sinkReference.set(sink);
        Sink<OUT> stage = sink;
        // 反向包装 -> 正向遍历
        if (reverse) {
            for (int i = 0; i <= sinkBuilders.size() - 1; i++) {
                Supplier<Sink<?>> supplier = sinkBuilders.get(i);
                stage = (Sink<OUT>) supplier.get();
            }
        } else {
            // 正向包装 -> 反向遍历
            for (int i = sinkBuilders.size() - 1; i >= 0; i--) {
                Supplier<Sink<?>> supplier = sinkBuilders.get(i);
                stage = (Sink<OUT>) supplier.get();
            }
        }
        Sink<OUT> finalStage = stage;
        spliterator.forEachRemaining(finalStage);
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(12);
        ReferenceChain<Integer, Integer> chain = new ReferenceChain<>();
        // filter -> map -> for each
        chain.filter(item -> item > 10)
                .map(item -> item * 2)
                .forEachPrint(list);
    }
}
