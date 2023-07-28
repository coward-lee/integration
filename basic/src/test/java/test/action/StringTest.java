package test.action;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class StringTest {
    @Test
    void test(){
        String s1 = "abc";
        String s2 = new String("abc");
        System.out.println(s2.getBytes(StandardCharsets.UTF_8) == s1.getBytes(StandardCharsets.UTF_8));
    }
}
