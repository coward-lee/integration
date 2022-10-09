package test.action;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Random;

import static org.mockito.Mockito.*;

/**
 * mock 会执行我们的 打桩
 * spy 会执行真实方法
 */
public class MockTest {

    @Test
    void test_() {
        System.out.println(LocalDate.ofEpochDay(19236));
        System.out.println(LocalDate.ofEpochDay(19037));
    }
}
