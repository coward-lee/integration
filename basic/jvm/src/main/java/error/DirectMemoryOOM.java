package error;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
@SuppressWarnings("restriction")
public class DirectMemoryOOM {
    private static final long _1MB = 1<<20;

    public static void main(String[] args) throws Throwable {
        Field field = Unsafe.class.getDeclaredFields()[0];
        field.setAccessible(true);
        Unsafe unsafe =(Unsafe) field.get(null);
        while(true){
            unsafe.allocateMemory(_1MB);
        }
    }

}
