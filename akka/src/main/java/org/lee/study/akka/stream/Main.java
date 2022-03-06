package org.lee.study.akka.stream;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.dispatch.Futures;
import akka.stream.ActorMaterializer;
import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.javadsl.*;
import akka.util.ByteString;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.CompletionStage;

public class Main {
    public static void main(String[] args) {
//        new Main().sinkDemo();
        new Main().flowDemo();
    }

    /**
     * 简单的整个流程
     */
    public void integrationDemo(){
        ActorSystem actorSystem = ActorSystem.create("sys");
        Materializer materializer = ActorMaterializer.create(actorSystem);
        Source<Integer, NotUsed> source = Source.range(1,5);
        Sink<Integer, CompletionStage<Done>> sink = Sink.foreach(System.out::println);
        RunnableGraph<NotUsed> graph = source.to(sink);
        graph.run(materializer);
    }

    /**
     * source 的实例
     * 构建 source
     */
    public void sourceDemo(){

        ActorSystem actorSystem = ActorSystem.create("sys");
        Materializer materializer = ActorMaterializer.create(actorSystem);
        // 从集合中创建source
        Source<String, NotUsed> source = Source.from(Arrays.asList("1", "2", "XASDASD"));
        source.runForeach(System.out::println, materializer);
        // 从future 创建
        Source<String, NotUsed> futureSource = Source.fromFuture(Futures.successful("hello world"));
        // 连续产生source
        Source<String, NotUsed> repeat = Source.repeat("hello");
        // 取出前面几个
        repeat.limit(10).runForeach(System.out::println, materializer);
        // 从文件的api产生一个source
        Source<ByteString, CompletionStage<IOResult>> byteStringCompletionStageSource = FileIO.fromPath(Paths.get("demo_in.txt"));
    }

    /**
     * sink 操作
     * 构建 sink
     */
    public void sinkDemo(){

        ActorSystem actorSystem = ActorSystem.create("sys");
        Materializer materializer = ActorMaterializer.create(actorSystem);
        // 循环遍历每个元素
        Sink<Object, CompletionStage<Done>> foreach = Sink.foreach(System.out::println);


        // 使用fold 函数创建 Sink
        // 并使用runWith会将Source和Sink连接起来并运行。
        Sink<Integer, CompletionStage<Integer>> fold = Sink.<Integer,Integer>fold(1,(x,y)-> x* y);
        CompletionStage<Integer> stage = Source.range(1, 5).runWith(fold, materializer);
        stage.thenAccept(System.out::println);

        // sink 做reduce操作
        Sink<Integer, CompletionStage<Integer>> reduce = Sink.reduce((x, y) -> x + y);
        CompletionStage<Integer> reduceStage = Source.range(1, 5).runWith(reduce, materializer);
        reduceStage.thenAccept(System.out::println);

        Sink<ByteString, CompletionStage<IOResult>> fileSink = FileIO.toPath(Paths.get("demo_out.txt"));
    }

    /**
     * 构建flow
     *
     */

    public void flowDemo(){

        ActorSystem actorSystem = ActorSystem.create("sys");
        Materializer materializer = ActorMaterializer.create(actorSystem);


        Flow<String, Integer, NotUsed> flow = Flow.of(String.class).map(x -> Integer.parseInt(x) * 3);
        Sink<Integer, CompletionStage<Done>> sink = Sink.foreach(System.out::println);
        Source.from(Arrays.asList("1","2")).via(flow).runWith(sink, materializer);
        Source<String, NotUsed> from = Source.from(Arrays.asList("1", "2", "3"));
        Source<Integer, NotUsed> viaSource = from.via(flow);
        viaSource.runWith(sink, materializer);

        Source.from(Arrays.asList("1","2","3")).runWith(flow.to(sink), materializer);
    }
}
