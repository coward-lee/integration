package org.lee.study.feature;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class JDK12 {
    @Test
    public void givenIdenticalFiles_thenShouldNotFindMismatch() throws IOException {
        Path filePath1 = Files.createTempFile("file1", ".txt");
        Path filePath2 = Files.createTempFile("file2", ".txt");
        Files.writeString(filePath1, "Java 12 Article");
        Files.writeString(filePath2, "Java 12 Article");

        long mismatch = Files.mismatch(filePath1, filePath2);
//        assertEquals(-1, mismatch);
    }

    /**
     * Teeing Collector
     */
    @Test
    public void givenSetOfNumbers_thenCalculateAverage() {
        double mean = Stream.of(1, 2, 3, 4, 5).collect(Collectors.teeing(
                Collectors.summingDouble(i -> i), // stream A
                Collectors.counting(),  // stream B
                (sum, count) -> sum / count
        ));
        assertEquals(3.0, mean, 0.1);
        Pair<Optional<Integer>, Optional<Integer>> collect = Stream.of(1, 2, 3, 4, 5).collect(Collectors.teeing(
                Collectors.minBy(Integer::compare), // stream A
                Collectors.maxBy(Integer::compare),  // stream B
                Pair::new
        ));
        int t = collect.t.orElseThrow();
        assertEquals(1, t);
        assertEquals(5, collect.r.orElseThrow().intValue());
    }

    @Test
    public void givenNumber_thenCompactValues() {
        NumberFormat likesShort =
                NumberFormat.getCompactNumberInstance(new Locale("en", "US"), NumberFormat.Style.SHORT);
        likesShort.setMaximumFractionDigits(2);
        System.out.println(likesShort.format(2592));
        assertEquals("2.59K", likesShort.format(2592));

        NumberFormat likesLong =
                NumberFormat.getCompactNumberInstance(new Locale("zh", "CH"), NumberFormat.Style.LONG);
        likesLong.setMaximumFractionDigits(2);
        System.out.println(likesLong.format(2592));
        assertEquals("2.59 thousand", likesLong.format(2592));
    }

    /**
     * switch
     */
    @Test
    public void test_switch() {
        // 以前的语法
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        String typeOfDay = "";
        switch (dayOfWeek) {
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
            case FRIDAY:
                typeOfDay = "Working Day";
                break;
            case SATURDAY:
            case SUNDAY:
                typeOfDay = "Day Off";
        }

        // JAVA 12
        // 具有返回值得
        typeOfDay = switch (dayOfWeek) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Working Day";
            case SATURDAY, SUNDAY -> "Day Off";
        };
        // 同样也可以在 case里面执行代码
        switch (dayOfWeek) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> System.out.println("Working Day");
            case SATURDAY, SUNDAY ->{
                System.out.println("Day Off");
                System.out.println("Day Off");
            }
        }
    }
    /**
     * instanceof 关键字优化
     */
    @Test
    public void test_instanceof(){
        Object o = "xxxx";
        if (o instanceof String str){
            System.out.println("string length :"+str.length());
        }
    }
}

class Pair<T, R> {
    T t;
    R r;

    public Pair(T t, R r) {
        this.t = t;
        this.r = r;
    }
}
