package org.lee.study.type;



import java.io.PrintStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class SuperTypeTest{
    public static void main(String[] args) throws Exception {
        List<? super Serializable> a = Arrays.asList(1.2 , 1, 2);
        PrintStream printStream = new PrintStream("a");
        printStream.println(a);
        // 编译错误
//        原因: 无法将 '<>' 与匿名内部类一起使用
//        Comparable<String> comparable = new Comparable<>() {@Override public int compareTo(String o) {return 0;}};
    }
}

