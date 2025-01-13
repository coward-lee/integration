package org.lee.guava.utilities;

import com.google.common.base.Strings;
import org.junit.jupiter.api.Test;

public class StringsTest {

    @Test
    void test(){
        String x = Strings.repeat("x", 3);
        System.out.println(x);
    }
}
