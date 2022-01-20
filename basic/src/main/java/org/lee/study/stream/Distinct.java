package org.lee.study.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/// stream 中的distinct方法
public class Distinct {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 2, 1, 2);
        List<Integer> collect = list.stream()
                .distinct()
                .collect(Collectors.toList());
        System.out.println(collect);
    }
}
