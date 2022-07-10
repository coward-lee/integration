package org.lee.study.feature;

import org.junit.Assert;
import org.junit.Test;
import reactor.util.annotation.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class JDK11 {
    @Test
    public void test_stream(){
        List<String> list = Stream.of("1", "2", "2", "")
                .filter(Predicate.not(String::isBlank))
                .toList();
        list.forEach(System.out::println);
    }

    @Test
    public void test_syntax_for_lambda(){
        List<String> sampleList = Arrays.asList("Java", "Kotlin");
        String resultString = sampleList.stream()
                .map((@NonNull var x) -> x.toUpperCase())
                .collect(Collectors.joining(", "));
        Assert.assertEquals(resultString,"JAVA, KOTLIN");
    }
}
