package varhandler;

import sun.misc.Unsafe;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class VarHandlerTest {

    String x;
    static Unsafe unsafe =  Unsafe.getUnsafe();

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        VarHandle varHandle = MethodHandles.arrayElementVarHandle(Point[].class);
        VarHandle x = MethodHandles.lookup().findVarHandle(Point.class, "x", int.class);
//        unsafe.compareAndSwapLong();

    }
    class Point {
        volatile int x;
        // ...
    }
    /**
     * o : 需要被修改变量属于的对象
     * offset ：属性位置的偏移量
     * expected：原来的值
     * x：新的值
     *  如果 对象o的属性的值等于expected那么修改为x，并返回true
     *     public final native boolean compareAndSetLong(
     *     Object o, long offset, long expected,  long x);
     *     这个存在ABA问题
     */
}
