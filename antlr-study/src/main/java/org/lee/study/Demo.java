package org.lee.study;

public class Demo {
    public static void main(String[] args) {
        Letter letter = new Letter("hello, jeff");
        String parse = letter.parse();
        System.out.println(parse);
    }
}
