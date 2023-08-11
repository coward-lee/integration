package test.action;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lee.study.util.FieldUtil;

import java.nio.charset.StandardCharsets;

public class StringTest {
    @Test
    void test() {
        String s1 = "abc";
        String s2 = new String("abc");
        System.out.println(s2.getBytes(StandardCharsets.UTF_8) == s1.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void test_str_memory_loop() {
        String s1 = "hello";
        String s2 = new String("hello");
        String s0 = "hel";
        String s3 = s0 + "lo";
        String s4 = new String("hel") + "lo";
        String s31 = (s0 + "lo").intern();
        String s42 = (new String("hel") + "lo").intern();
//        System.out.println("s1==s2 is " + ( == s2));
        System.out.println("s1 " + FieldUtil.getValue(String.class, "value", s1));
        System.out.println("s2 " + FieldUtil.getValue(String.class, "value", s2));
        System.out.println("s0 " + FieldUtil.getValue(String.class, "value", s0));
        System.out.println("s3 " + FieldUtil.getValue(String.class, "value", s3));
        System.out.println("s4 " + FieldUtil.getValue(String.class, "value", s4));
        System.out.println("s31" + FieldUtil.getValue(String.class, "value", s31));
        System.out.println("s42" + FieldUtil.getValue(String.class, "value", s42));

        Assertions.assertEquals((byte[])FieldUtil.getValue(String.class, "value", s1), FieldUtil.getValue(String.class, "value", s1));
        Assertions.assertEquals((byte[])FieldUtil.getValue(String.class, "value", s1), FieldUtil.getValue(String.class, "value", s2));
        Assertions.assertNotEquals((byte[])FieldUtil.getValue(String.class, "value", s1), FieldUtil.getValue(String.class, "value", s0));
        Assertions.assertNotEquals((byte[])FieldUtil.getValue(String.class, "value", s1), FieldUtil.getValue(String.class, "value", s3));
        Assertions.assertNotEquals((byte[])FieldUtil.getValue(String.class, "value", s1), FieldUtil.getValue(String.class, "value", s4));
        Assertions.assertEquals((byte[])FieldUtil.getValue(String.class, "value", s1), FieldUtil.getValue(String.class, "value", s31));
        Assertions.assertEquals((byte[])FieldUtil.getValue(String.class, "value", s1), FieldUtil.getValue(String.class, "value", s42));

    }
}
