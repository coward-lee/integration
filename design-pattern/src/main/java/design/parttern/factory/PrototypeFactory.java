package design.parttern.factory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 原型类
 * 就是有一个基本的类，但是每次的对类进行使用的使用可复制一个对象出去给调用者调用
 */
public class PrototypeFactory {
    Map<String, Object> map = new HashMap<>();
    Object getObj(String name) throws Exception {
        Object instance = map.get(name);
        if (instance == null){
            instance = new PrototypeObj("原型",new RefClass("原型的应用类"));
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(instance);

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        return ois.readObject();
    }

    public static void main(String[] args) throws Exception {
        PrototypeFactory prototypeFactory = new PrototypeFactory();
        Object as = prototypeFactory.getObj("as");
        System.out.println(as);
        System.out.println( prototypeFactory.getObj("as"));
        System.out.println( prototypeFactory.getObj("as"));
        System.out.println( prototypeFactory.getObj("as"));
    }

    public static class PrototypeObj implements Serializable{
        public PrototypeObj(String name, RefClass refClass) {
            this.name = name;
            this.refClass = refClass;
        }

        String name;
        RefClass refClass;

        @Override
        public String toString() {
            return "PrototypeObj{" +
                    "name='" + name + '\'' +
                    ", refClass=" + refClass +
                    '}';
        }


    }
    public static class RefClass implements Serializable{
        public RefClass(String ref) {
            this.ref = ref;
        }

        String ref;

        @Override
        public String toString() {
            return "RefClass{" +
                    "ref='" + ref + '\'' +
                    '}';
        }
    }


}



