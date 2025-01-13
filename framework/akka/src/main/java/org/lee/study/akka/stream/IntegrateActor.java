package org.lee.study.akka.stream;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.OverflowStrategy;
import akka.stream.SinkRef;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

public class IntegrateActor {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("sys");
        Materializer materializer = ActorMaterializer.create(actorSystem);
        Source<Object, NotUsed> source = Source.actorRef(10, OverflowStrategy.fail()).mapMaterializedValue(ref -> {
            ref.tell("AAA", ActorRef.noSender());
            ref.tell("BBB", ActorRef.noSender());
            ref.tell("CCC", ActorRef.noSender());
            ref.tell("DDD", ActorRef.noSender());
            ref.tell("EEE", ActorRef.noSender());
            return NotUsed.getInstance();
        });

        ActorRef sinkActor = actorSystem.actorOf(Props.create(SinkRef.class), "sinkActor");
        Sink<Object, NotUsed> sink = Sink.actorRef(sinkActor, "succ");
        source.runWith(sink, materializer);
    }
}
