import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import akka.stream.javadsl.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class AkkaStreamTest {


    ActorSystem system;
    Materializer materializer;

    @BeforeEach
    public void before() {
        system = ActorSystem.create("AkkaStreamTest");
        materializer = Materializer.matFromSystem(system);
    }

    /**
     * 通过   Source::toMat, 连接 Source 到 Sink
     * 这个方法返回一个  RunnableGraph
     */
    @Test
    void test_to_mat() {
        final Source<Integer, NotUsed> source =
                Source.from(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        // note that the Future is scala.concurrent.Future
        final Sink<Integer, CompletionStage<Integer>> sink = Sink.fold(0, (i, j) -> {
            System.out.println("数字：" + i + "+" + j);
            return (i + j);
        });
        // connect the Source to the Sink, obtaining a RunnableFlow
        final RunnableGraph<CompletionStage<Integer>> runnable = source.toMat(sink, Keep.right());
        // materialize the flow
        final CompletionStage<Integer> sum = runnable.run(materializer);
        sum.toCompletableFuture().thenApply(i -> {
            System.out.println(i);
            return Done.done();
        });
        Assertions.assertTrue(true);
    }

    @Test
    void test_runWith_with_actorSystem() throws InterruptedException {
        final Source<Integer, NotUsed> source =
                Source.from(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        final Sink<Integer, CompletionStage<Integer>> sink = Sink.fold(0, (i, j) -> {
            System.out.println("数字：" + i + "+" + j);
            return (i + j);
        });
        // materialize the flow, getting the Sink's materialized value
        final CompletionStage<Integer> sum = source.runWith(sink, materializer);

        Thread.sleep(100);
        Assertions.assertTrue(true);
    }

    @Test
    void test_run() throws InterruptedException {
        // connect the Source to the Sink, obtaining a RunnableGraph
        final Sink<Integer, CompletionStage<Integer>> sink = Sink.fold(0, (i, j) -> {
            System.out.println("数字：" + i + "+" + j);
            return (i + j);
        });
        final RunnableGraph<CompletionStage<Integer>> runnable =
                Source.from(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).toMat(sink, Keep.right());

// get the materialized value of the FoldSink
        final CompletionStage<Integer> sum1 = runnable.run(materializer);
        final CompletionStage<Integer> sum2 = runnable.run(materializer);

        Thread.sleep(100);
        Assertions.assertTrue(true);
    }

    @Test
    void test_sink_combine() {
//        Sink<Integer, NotUsed> sendRemotely = Sink.;
//        Sink<Integer, CompletionStage<Done>> localProcessing =
//                Sink.foreach(
//                        a -> System.out.println(a+"  "));
//        Sink<Integer, NotUsed> sinks =
//                Sink.combine(sendRemotely, localProcessing, new ArrayList<>(), a -> Broadcast.create(a));
//
//        Source.from(Arrays.asList(new Integer[] {0, 1, 2})).runWith(sinks, system);
//        Thread.sleep(2000);
    }

    @Test
    void test_sink_flod() {
        final Sink<Integer, CompletionStage<Integer>> foldSink =
                Sink.fold(0, Integer::sum);
        Source.from(IntStream.range(1, 100).boxed().collect(Collectors.toList()))
                .runWith(foldSink, materializer);
    }

    @Test
    void test_Source_and_Sink() throws Exception {
        Source<Integer, NotUsed> from = Source.from(List.of(1, 2, 3, 4, 5));
        Sink<Integer, CompletionStage<Integer>> reduce = Sink.<Integer>reduce((a, b) -> {
            System.out.println(a + ":" + b);
            return a * 10 + b * 10;
        });
        // 可以返回一个空的结果
        Source<Integer, CompletableFuture<Optional<Integer>>> source = Source.maybe();
        //
        Flow<Integer, Integer, NotUsed> flow = Flow.fromSinkAndSource(reduce, from);
//         返回第一个元素的sink
        Sink<Integer, CompletionStage<Integer>> sink = Sink.head();
//        默认情况下，保留最左侧阶段的实际值
//        RunnableGraph<CompletableFuture<Optional<Integer>>> r1 = source.via(flow).to(sink);

        RunnableGraph<NotUsed> r2 = source.viaMat(flow, Keep.right()).to(sink);
//        Source.fromGraph(r2).runForeach(System.out::println, materializer);

        RunnableGraph<CompletionStage<Integer>> r3 = source.via(flow).toMat(sink, Keep.right());

        Integer integer = r3.run(materializer).toCompletableFuture().get();
        System.out.println(integer);
        Thread.sleep(1000);
    }

    @Test
    void test_Source_and_Sink_01() throws Exception {
        Source<Integer, NotUsed> from1 = Source.from(List.of(1, 2, 3, 4, 5));
        Source<Integer, NotUsed> from2= Source.from(List.of(1, 2, 3, 4, 5));
        Sink<String, CompletionStage<Done>> foreach = Sink.foreach(System.out::println);

        Sink<Integer, CompletionStage<List<Integer>>> reduce = Sink.seq();



        Flow<Integer, Integer, NotUsed> flow = Flow.fromSinkAndSource(reduce, from1);
        Source<Integer, NotUsed> source = from2.viaMat(flow, Keep.left());

//        runnableGraph01.run(materializer);
//        RunnableGraph<CompletionStage<Done>> runnableGraph02 = from.viaMat(flow, Keep.right()).toMat(foreach, Keep.right());
//
//
//
//        RunnableGraph<CompletionStage<Integer>> r3 = from.via(flow).toMat(reduce, Keep.right());
//        Integer integer = r3.run(materializer).get();
//        System.out.println(integer);

    }
}
