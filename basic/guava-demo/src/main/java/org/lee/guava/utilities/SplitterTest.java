package org.lee.guava.utilities;

import com.google.common.base.Splitter;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SplitterTest {

    @Test
    void test_split(){
        List<String> split = Splitter.on('|').splitToList("hello|world");
        split.forEach(System.out::println);
        Splitter.on('|').omitEmptyStrings().splitToList("hello|world|||||xxx||x")
                .forEach(System.out::println);

    }
}
