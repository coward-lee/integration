package org.lee.study.feature;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JDK10 {
    List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

    @Test
    public void test_copy_of(){
        // 返回一个不可边的集合
        List<Integer> integers = List.copyOf(list);
        integers.add(3);
    }
    @Test(expected = UnsupportedOperationException.class)
    public void whenModifyToUnmodifiableList_thenThrowsException() {
        List<Integer> evenList = list.stream()
                .filter(i ->( i % 2 == 0))
                .collect(Collectors.toUnmodifiableList());
        evenList.add(4);
    }
    @Test
    public void whenListContainsInteger_OrElseThrowReturnsInteger() {
        Integer firstEven = list.stream()
                .filter(i -> i % 2 == 0)
                .findFirst()
                .orElseThrow();
        System.out.println(firstEven);
    }
}
