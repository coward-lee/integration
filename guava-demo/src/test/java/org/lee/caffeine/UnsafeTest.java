package org.lee.caffeine;

import org.jctools.util.UnsafeAccess;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class UnsafeTest {
    public static final Unsafe UNSAFE;

    static {
        UNSAFE = getUnsafe();
    }

    @Test
    void demo() {
        try {

            int i = 1 << 4;
            long[] ls = new long[i];
            int scale = UnsafeAccess.UNSAFE.arrayIndexScale(long[].class);
            int offset = UNSAFE.arrayBaseOffset(long[].class);
            for (int j = 0; j < i; j++) {
                UNSAFE.putOrderedObject(ls, offset, 1L<<32);
                offset += scale;
            }
            for (long l : ls) {
                System.out.println(l);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Test
    void demo1() {
        try {

            int i = 1 << 4;
            Long[] ls = new Long[i];
            int offset = UNSAFE.arrayBaseOffset(Long[].class);
//            UNSAFE.putOrderedObject(ls, offset, Long.MAX_VALUE);
//            UNSAFE.putOrderedObject(ls, offset + 8, Long.MAX_VALUE);
            int scale = UnsafeAccess.UNSAFE.arrayIndexScale(long[].class);


            for (int j = 0; j < i; j++) {
                UNSAFE.putOrderedObject(ls, offset, Long.MAX_VALUE);
                offset += scale;
            }
            for (Long l : ls) {
                System.out.println(l);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static Unsafe getUnsafe() {
        Unsafe instance;
        try {
            final Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            instance = (Unsafe) field.get(null);
        } catch (Exception ignored) {
            // Some platforms, notably Android, might not have a sun.misc.Unsafe implementation with a private
            // `theUnsafe` static instance. In this case we can try to call the default constructor, which is sufficient
            // for Android usage.
            try {
                Constructor<Unsafe> c = Unsafe.class.getDeclaredConstructor();
                c.setAccessible(true);
                instance = c.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }
}
