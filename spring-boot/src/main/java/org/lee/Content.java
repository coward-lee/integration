package org.lee;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.*;
import java.io.Serializable;

//@Table(schema = "shading_table")
@Entity(name = "shading_table")
public class Content implements ContentInter{
    @Id
    private String id;
    @Convert(converter = ContentConverter.class)
    private SuperInter content;

    public Content(String id, SuperInter content) {
        this.id = id;
        this.content = content;
    }

    public Content() {

    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = NUMDemo.class, name = "ENUM"),
            @JsonSubTypes.Type(value = ClassDemo.class, name = "CLASS"),
    })
    interface SuperInter extends Serializable {

    }

    enum NUMDemo implements SuperInter {
        TEST("ENUM_CONTENT");
        final String content;
        NUMDemo(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "NUMDemo{" +
                    "content='" + content + '\'' +
                    '}';
        }
    }
    static class ClassDemo implements SuperInter {
        private String demo;

        public ClassDemo() {
        }

        public ClassDemo(String demo) {
            this.demo = demo;
        }

        @Override
        public String toString() {
            return "ClassDemo{" +
                    "demo='" + demo + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Content{" +
                "id='" + id + '\'' +
                ", content=" + content +
                '}';
    }
}
