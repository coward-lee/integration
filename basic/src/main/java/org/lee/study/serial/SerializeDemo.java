package org.lee.study.serial;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SerializeDemo {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.println("aaa");
        scanner.next();
        int n = 10000;
        for (int j = 0; j < 10; j++) {
            int finalJ = j;
            new Thread(()->{
                try {
                    writeFile(n, finalJ);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }
    public static void writeFile(int n, int threadNum) throws IOException {
        for (int i = 0; i < n; i++) {
//            System.out.println(i);
//            File file = new File("demo");
//            FileOutputStream fileOutputStream = new FileOutputStream(file);
//            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
//            Demo demo = new Demo("222222222","namenamenamenamenamename",i,Boolean.FALSE);
//            out.writeObject(demo);
//            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
//            Object o = inputStream.readObject();
//            System.out.println(o==null);

            Demo demo = new Demo("222222222", "namenamenamenamenamename", i, Boolean.FALSE);
//            System.out.println(i);
            File file = new File("demo"+threadNum);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write((
                            "id:" + demo.id + "\n" +
                                    "name:"+demo.name+"\n"+
                                    "age:"+demo.age+"\n"+
                                    "gender:"+demo.gender
                    ).getBytes(StandardCharsets.UTF_8)
            );
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int read = fileInputStream.read(bytes);
            String s = new String(bytes, 0, read,StandardCharsets.UTF_8);
            String[] split = s.split("\n");
            String id = split[0].replace("id:", "");
            String name = split[1].replace("name:", "");
            String age = split[2].replace("age:", "");
            String gender = split[3].replace("gender:", "");
            Demo demo1 = new Demo(id, name, Integer.valueOf(age), Boolean.valueOf(gender));
//            System.out.println(demo1);
        }
    }

    public static void testBuffer() throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.println("aaa");
        scanner.next();
        int n = 1000000;
        for (int i = 0; i < n; i++) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
            Demo demo = new Demo("222222222", "namenamenamenamenamename", i, Boolean.FALSE);
            out.writeObject(demo);
            ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            Object o = inputStream.readObject();
            System.out.println(demo);
        }
    }
    static class Demo implements Serializable {

        private static final long serialVersionUID = 1L;
        String id;
        String name;
        Integer age;
        Boolean gender;

        public Demo(String id, String name, Integer age, Boolean gender) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.gender = gender;
        }

        @Override
        public String toString() {
            return "Demo{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    ", gender=" + gender +
                    '}';
        }
    }
}
