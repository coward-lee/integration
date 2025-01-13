package org.lee.study.spring.no.circle;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

public class NoCircle {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        NoCircleA a = (NoCircleA) context.getBean("noCircleA");
        System.out.println(a.getB());
    }


}
@Configuration
@ComponentScan(basePackages = "org.lee.study.spring.no.circle")
class Config{

}

@Component
class NoCircleA{
    private final NoCircleB b;

    public NoCircleB getB() {
        return b;
    }

    public NoCircleA(@Lazy NoCircleB b) {
        this.b = b;
    }
}
@Component
class NoCircleB{

    private final NoCircleC c;

    public NoCircleB(@Lazy  NoCircleC c) {
        this.c = c;
    }
}
@Component
class NoCircleC{

    private final NoCircleA a;

    public NoCircleC( NoCircleA a) {
        this.a = a;
    }
}
