package org.lee.study.akka.stream.mysql;

import akka.NotUsed;
import akka.stream.alpakka.slick.javadsl.Slick;
import akka.stream.alpakka.slick.javadsl.SlickRow;
import akka.stream.alpakka.slick.javadsl.SlickSession;
import akka.stream.javadsl.Source;

public class MysqlSource {
    private static final SlickSession session = SlickSession.forConfig("slick-mysql");
    public static Source<StreamMysql.DemoPojo, NotUsed> toSource() {
        return Slick.source(session, "select * from shading_table",
                (SlickRow slickRow) ->
                {
                    StreamMysql.DemoPojo demoPojo = new StreamMysql.DemoPojo(slickRow.nextLong(), slickRow.nextString());
                    System.out.println(demoPojo);
                    return demoPojo;
                });
    }
}
