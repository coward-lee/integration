package org.lee;

import java.security.SecureRandom;
import java.util.UUID;

public class UUIDDemo {


    public static void main(String[] args) {
//        1100  000000000000000
//        000000000000
        UUID uuid = UUID.randomUUID();
//        long variant = ((long) uuid.variant()) << (15 * 4);
//        long clockSequence = ((long)uuid.clockSequence()) << (12*4);
//        long node = uuid.node();
//        System.out.printf("%x  %x   %x  ",uuid.variant(),uuid.clockSequence(), uuid.node());
        System.out.println(uuid);
        System.out.println(uuid.getLeastSignificantBits());
        System.out.println(uuid.getMostSignificantBits());
    }
}
