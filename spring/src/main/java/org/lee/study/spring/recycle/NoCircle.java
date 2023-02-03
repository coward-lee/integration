package org.lee.study.spring.recycle;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

public class NoCircle {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigA.class);
        NoCircleA a = (NoCircleA) context.getBean("noCircleA");
        System.out.println(a.getB());
    }


}
@Configuration
@ComponentScan(basePackages = "org.lee.study.spring.recycle")
class ConfigA{

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

    private final NoCircleA a;

    public NoCircleB( @Lazy NoCircleA a) {
        this.a = a;
    }
}
//@Component
//class NoCircleC{
//
//    private final NoCircleA a;
//
//    public NoCircleC( @Lazy NoCircleA a) {
//        this.a = a;
//    }
//}
