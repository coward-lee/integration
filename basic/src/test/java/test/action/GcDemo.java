package test.action;

import org.junit.jupiter.api.Test;

public class GcDemo {

    @Test
    void gc_log_info() {
        int _1MB = 1024 * 1024;
        byte[] b1, b2, b3, b4;
        b1 = new byte[_1MB];
        System.out.println("b1b1b1b1b1b1b1b1b1b1b1b1b1b1b1b1b1b1b1b1b1b1b1b1b1b1b1b1b1b1");
        b2 = new byte[_1MB];
//        System.out.println("b2b2b2b2b2b2b2b2b2b2b2b2b2b2b2b2b2b2b2b2b2b2b2b2b2b2b2b2b2b2b2");
        b3 = new byte[_1MB];
        System.out.println("b1");
        b4 = new byte[_1MB];
        System.out.println("b1");
    }
}
