package org.lee.study.stream;


import java.util.*;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.*;

/**
 * Spliterator 创建流的关键开始
 */
public class StreamTest {

    /**
     * 为了输出一个 stream的流程图
     */
    public void stream_for_output() {
        List<String> a = List.of("a");
        List<String> collect = a.stream()
                .filter(d -> true)
                .map(d -> d)
                .collect(Collectors.toList());


    }


}


/**
 * 名词解释
 * Collection 集合类
 * Stream 流
 * Spliterator  可拆分迭代器Spliterator Stream的源头，如Collection的Spliterator； 和这个与 Iterator，功能相似 但是比Iterator功能更强，如Spliterator是平行流的关键
 * StreamSupport 工具类
 * ReferencePipeline。 具体的流链条上的具体操作的持有类
 * 操作符 ： stream 中间的操作过程 如 map， filter 产生的实例
 * Sink 操作
 * stream 操作中的 characteristics 用来表示整个流的一些特性：是否已经排序，是否可变等
 */

public interface Spliterator<T> {
    // 迭代操作
    boolean tryAdvance(Consumer<? super T> action);

    default void forEachRemaining(Consumer<? super T> action) {
        do { } while (tryAdvance(action));
    }

    java.util.Spliterator<T> trySplit();

    long estimateSize();


    default long getExactSizeIfKnown() {
        return (characteristics() & SIZED) == 0 ? -1L : estimateSize();
    }

    int characteristics();

    default boolean hasCharacteristics(int characteristics) {
        return (characteristics() & characteristics) == characteristics;
    }

    default Comparator<? super T> getComparator() {
        throw new IllegalStateException();
    }
}
/**
 * Stream Collection#stream() 创建一个stream
 * 这个最后返回的是是一个 ReferencePipeline.Head
 * 对下面的类解释
 */
public interface Collection<E> extends Iterable<E> {
    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, 0);
    }
}

/**
 * 创建 spliterator
 */
public final class Spliterators {
    //  * Spliterator  可拆分迭代器Spliterator Stream的源头，如Collection的Spliterator； 和这个与 Iterator，功能相似 但是比Iterator功能更强，如Spliterator是平行流的关键
    public static <T> Spliterator<T> spliterator(java.util.Collection<? extends T> c, int characteristics) {
        return new java.util.Spliterators.IteratorSpliterator<>(Objects.requireNonNull(c), characteristics);
    }
}

public final class StreamSupport {

    public static <T> Stream<T> stream(Spliterator<T> spliterator, boolean parallel) {
        Objects.requireNonNull(spliterator);
        return new ReferencePipeline.Head<>(spliterator,
                StreamOpFlag.fromCharacteristics(spliterator),
                parallel);
    }
}


/**
 * <pre>
 * Stream Stream#filter(Predicate) 这里其实只是实例化了一个 无状态的操作符，同时将 this 作为 upStream(前驱节点)
 * Stream Stream#map(Function) 这里其实只是实例化了一个 无状态的操作符，
 * List&lt;String&gt; Stream#.collect(Collectors.toList())
 */
class ReferencePipeline<P_IN, P_OUT> extends AbstractPipeline<P_IN, P_OUT, Stream<P_OUT>> implements Stream<P_OUT> {
    public final Stream<P_OUT> filter(Predicate<? super P_OUT> predicate) {
        return new StatelessOp<P_OUT, P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SIZED) {
            Sink<P_OUT> opWrapSink(int flags, Sink<P_OUT> sink) {
                return new Sink.ChainedReference<P_OUT, P_OUT>(sink) {
                    public void begin(long size) {
                        downstream.begin(-1);
                    }

                    public void accept(P_OUT u) {
                        if (predicate.test(u)) downstream.accept(u);
                    }
                };
            }
        };
    }

    public final <R> Stream<R> map(Function<? super P_OUT, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        return new java.util.stream.ReferencePipeline.StatelessOp<P_OUT, R>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            java.util.stream.Sink<P_OUT> opWrapSink(int flags, java.util.stream.Sink<R> sink) {
                return new java.util.stream.Sink.ChainedReference<P_OUT, R>(sink) {
                    @Override
                    public void accept(P_OUT u) {
                        downstream.accept(mapper.apply(u));
                    }
                };
            }
        };
    }

    /**
     * 这里作重要的方法： evaluate 他会调用 ReduceOp 来进行收集操作（其实在本次例子中，他会invoke 原始集合的Spliterator#tryAdvance方法）
     */
    public final <R, A> R collect(Collector<? super P_OUT, A, R> collector) {
        A container;
        if (isParallel()
                && (collector.characteristics().contains(Collector.Characteristics.CONCURRENT))
                && (!isOrdered() || collector.characteristics().contains(Collector.Characteristics.UNORDERED))) {
            container = collector.supplier().get();
            BiConsumer<A, ? super P_OUT> accumulator = collector.accumulator();
            forEach(u -> accumulator.accept(container, u));
        } else {
            container = evaluate(ReduceOps.makeRef(collector));
        }
        return collector.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)
                ? (R) container
                : collector.finisher().apply(container);
    }

    final <R> R evaluate(TerminalOp<E_OUT, R> terminalOp) {
        assert getOutputShape() == terminalOp.inputShape();
        if (linkedOrConsumed)
            throw new IllegalStateException(MSG_STREAM_LINKED);
        linkedOrConsumed = true;

        return isParallel()
                ? terminalOp.evaluateParallel(this, sourceSpliterator(terminalOp.getOpFlags()))
                : terminalOp.evaluateSequential(this, sourceSpliterator(terminalOp.getOpFlags()));
    }

    /**
     *  terminalOp.evaluateSequential 方法会再次调回来
     */

    final <P_IN, S extends Sink<E_OUT>> S wrapAndCopyInto(S sink, Spliterator<P_IN> spliterator) {
        copyInto(wrapSink(Objects.requireNonNull(sink)), spliterator);
        return sink;
    }

    final <P_IN> void copyInto(java.util.stream.Sink<P_IN> wrappedSink, Spliterator<P_IN> spliterator) {
        Objects.requireNonNull(wrappedSink);

        if (!StreamOpFlag.SHORT_CIRCUIT.isKnown(getStreamAndOpFlags())) {
            wrappedSink.begin(spliterator.getExactSizeIfKnown());
            spliterator.forEachRemaining(wrappedSink);
            wrappedSink.end();
        }
        else {
            copyIntoWithCancel(wrappedSink, spliterator);
        }
    }
    @SuppressWarnings("unchecked")
    final <P_IN> boolean copyIntoWithCancel(java.util.stream.Sink<P_IN> wrappedSink, Spliterator<P_IN> spliterator) {
        @SuppressWarnings({"rawtypes","unchecked"})
        AbstractPipeline p = AbstractPipeline.this;
        while (p.depth > 0) {
            p = p.previousStage;
        }

        wrappedSink.begin(spliterator.getExactSizeIfKnown());
        boolean cancelled = p.forEachWithCancel(spliterator, wrappedSink);
        wrappedSink.end();
        return cancelled;
    }


}


final class ReduceOps {
    private abstract static class Box<U> {
        U state;

        Box() {} // Avoid creation of special accessor

        public U get() {
            return state;
        }
    }
    public static <T, I> TerminalOp<T, I>  makeRef(Collector<? super T, I, ?> collector) {
        Supplier<I> supplier = Objects.requireNonNull(collector).supplier();
        BiConsumer<I, ? super T> accumulator = collector.accumulator();
        BinaryOperator<I> combiner = collector.combiner();
        class ReducingSink extends java.util.stream.ReduceOps.Box<I>
                implements java.util.stream.ReduceOps.AccumulatingSink<T, I, ReducingSink> {
            @Override
            public void begin(long size) {
                state = supplier.get();
            }

            @Override
            public void accept(T t) {
                accumulator.accept(state, t);
            }

            @Override
            public void combine(ReducingSink other) {
                state = combiner.apply(state, other.state);
            }
        }
        return new java.util.stream.ReduceOps.ReduceOp<T, I, ReducingSink>(StreamShape.REFERENCE) {
            @Override
            public ReducingSink makeSink() {
                return new ReducingSink();
            }

            @Override
            public int getOpFlags() {
                return collector.characteristics().contains(Collector.Characteristics.UNORDERED)
                        ? StreamOpFlag.NOT_ORDERED
                        : 0;
            }
        };
    }


    @Override
    public <P_IN> R evaluateSequential(PipelineHelper<T> helper,
                                       Spliterator<P_IN> spliterator) {
        return helper.wrapAndCopyInto(makeSink(), spliterator).get();
    }
}


/**
 * Collectors.toList()
 */

class Collectors {
    public static <T>
    Collector<T, ?, List<T>> toList() {
        CollectorImpl<> (ArrayList::new, List::add, (left, right) -> {
            left.addAll(right);
            return left;
        }, CH_ID);
    }
}

