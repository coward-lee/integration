package org.lee.study.feature;

//import jdk.incubator.vector.IntVector;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static java.lang.ClassLoader.getSystemClassLoader;
import static org.junit.Assert.assertEquals;

public class JDK16 {
    /**
     * invoke default method
     * @throws Throwable
     */
    @Test
    public void test_interface() throws Throwable{
        interface HelloWorld {
            default String hello() {
                return "world";
            }
        }
        Object proxy = Proxy.newProxyInstance(getSystemClassLoader(), new Class<?>[] { HelloWorld.class },
                (prox, method, args) -> {
                    if (method.isDefault()) {
                        return InvocationHandler.invokeDefault(prox, method, args);
                    }
                    // ...
                    return prox;
                }
        );
        Method method = proxy.getClass().getMethod("hello");
        assertEquals(method.invoke(proxy), "world");
    }
    /**
     * 一个日期api
     */
    @Test
    public  void test_date(){
        LocalTime date = LocalTime.parse("15:25:08.690791");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h B");
        assertEquals(date.format(formatter), "3 下午");
    }

    /**
     * 矢量api
     */
    @Test
    public void test_vector(){
        int[] a = {1, 2, 3, 4};
        int[] b = {5, 6, 7, 8};

        var c = new int[a.length];
// old
//        for (int i = 0; i < a.length; i++) {
//            c[i] = a[i] * b[i];
//        }

//        var vectorA = IntVector.fromArray(IntVector.SPECIES_128, a, 0);
//        var vectorB = IntVector.fromArray(IntVector.SPECIES_128, b, 0);
//        var vectorC = vectorA.mul(vectorB);
//        vectorC.intoArray(c, 0);
    }

}
