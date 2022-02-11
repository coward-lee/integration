package org.lee.study.refleact;

import java.lang.reflect.Constructor;

public class GenerateObjectFromPojo {
    public static void main(String[] args) throws Throwable {
        String json = "id:111";
        String sub = "id:";
        Class<Person> clazz = (Class<Person>) Class.forName(Person.class.getName());
        ClassLoader classLoader = clazz.getClassLoader();
        System.out.println(classLoader.toString());
        Constructor<?> constructor = clazz.getConstructor(Integer.class);
        Object o = constructor.newInstance(Integer.parseInt(json.substring(json.indexOf(sub)+"id:".length())));
        System.out.println(o);

    }

    private static class Person{
        Integer id;

        public Person() {
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Person(Integer id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    '}';
        }
    }
}
