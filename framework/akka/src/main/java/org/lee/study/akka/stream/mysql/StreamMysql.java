package org.lee.study.akka.stream.mysql;

import akka.Done;
import akka.NotUsed;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.*;
import akka.actor.ActorSystem;

import java.util.concurrent.CompletionStage;


public class StreamMysql {

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("sys");

        Sink<DemoPojo, CompletionStage<Done>> foreach = Sink.foreach(System.out::println);
        RunnableGraph<NotUsed> runnable = MysqlSource.toSource().to(foreach);
//        MysqlSource.toSource().log("user").runWith(Sink.ignore(), actorSystem);
        Materializer materializer = ActorMaterializer.create(actorSystem);
        runnable.run(materializer);
    }

    static class DemoPojo{
        Long id;
        String content;

        public DemoPojo(Long id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return "DemoPojo{" +
                    "id=" + id +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
}
