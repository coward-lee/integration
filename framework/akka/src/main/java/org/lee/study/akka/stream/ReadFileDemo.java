package org.lee.study.akka.stream;


import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.javadsl.*;
import akka.util.ByteString;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletionStage;

public class ReadFileDemo {
    public static void main(String[] args) {
        new ReadFileDemo().readAndConvertThenWriteToAnother();
    }

    public void readAndConvertThenWriteToAnother(){
        ActorSystem actorSystem = ActorSystem.create("sys");
        Materializer materializer = ActorMaterializer.create(actorSystem);
        Path path = Paths.get("access_log.txt");
        // 从文件读取数据 Source
        Source<ByteString, CompletionStage<IOResult>> fileSource = FileIO.fromPath(path);
        // 被写的文件
        Sink<ByteString, CompletionStage<IOResult>> fileSink = FileIO.toPath(Paths.get("demo_out.txt"));
        // 读出文件的flow的第一个操作
        Flow<ByteString, String, NotUsed> readFromFile = Framing.delimiter(ByteString.fromString("\r\n"), 100).map(x -> x.utf8String());

        // 中间操作
        Flow<String, String, NotUsed> outMid = Flow.of(String.class).map(x -> {
            String[] split = x.split(":");
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < split.length - 1; i++) {
                System.out.print(split[i] + ":=:");
                builder.append(split[i]).append("=");
            }

            System.out.println(split[split.length - 1]);
            builder.append(split[split.length - 1]);
            return builder.toString();
        });

        // 中间操作
        Flow<String, ByteString, NotUsed> writeToFile =Flow.of(String.class).map(x -> ByteString.fromString(x + "\r\n"));

        RunnableGraph<CompletionStage<IOResult>> to = fileSource.via(readFromFile).via(outMid).via(writeToFile).to(fileSink);
        to.run(materializer);
    }

    /**
     * 读写，也就是复制
     */
    public void justCopy(){
        ActorSystem actorSystem = ActorSystem.create("sys");
        Materializer materializer = ActorMaterializer.create(actorSystem);
        Path path = Paths.get("access_log.txt");
        // 从文件读取数据 Source
        Source<ByteString, CompletionStage<IOResult>> fileSource = FileIO.fromPath(path);
        // 写数据到文件 Sink
        // 直接写
        Sink<ByteString, CompletionStage<IOResult>> fileSink = FileIO.toPath(Paths.get("demo_out.txt"));
        RunnableGraph<CompletionStage<IOResult>> graph = fileSource.to(fileSink);
        CompletionStage<IOResult> run = graph.run(materializer);
        run.thenAccept(System.out::println);

    }
}
