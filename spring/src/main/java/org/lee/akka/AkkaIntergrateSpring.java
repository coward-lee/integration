package org.lee.akka;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Field;

public class AkkaIntergrateSpring {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(Config.class);
        Actor actor = new Actor();
        Class<? extends Actor> clazz = actor.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String name = field.getName();
                Object item = context.getBean(name, field.getType());
                field.set(actor,item);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        actor.a();
        actor.b();
    }
}
