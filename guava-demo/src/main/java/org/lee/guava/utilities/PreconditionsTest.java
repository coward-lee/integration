package org.lee.guava.utilities;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PreconditionsTest {

    @Test
    public void checkNotNull(){
        List<String> list = List.of("1","2");
        List<String> strings = Preconditions.checkNotNull(list);
        strings.forEach(System.out::println);
    }
    @Test
    void test_null(){
//        Objects.hashCode()
    }
}
