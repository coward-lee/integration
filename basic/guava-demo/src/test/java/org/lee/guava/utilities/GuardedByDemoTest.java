package org.lee.guava.utilities;

import com.google.errorprone.annotations.concurrent.GuardedBy;
import org.apache.commons.lang3.ThreadUtils;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

public class GuardedByDemoTest {

    @Test
    void test_GuardedBy() {

        GuardedByDemo guardedByDemo = new GuardedByDemo();
        guardedByDemo.ii();
        guardedByDemo.noIi();
    }

    @GuardedBy("this")
    static void testGuardedBy(String in, int s1, int s2) {
        try {
            ThreadUtils.sleep(Duration.ofSeconds(s1));
            System.out.printf("%s in:%s%n", LocalDateTime.now(), in);
            ThreadUtils.sleep(Duration.ofSeconds(s2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
