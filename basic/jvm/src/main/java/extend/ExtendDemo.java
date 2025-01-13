package extend;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ExtendDemo {
    public static void main(String[] args) {
        GrandFather gg = new Son();
        gg.think();
    }
}

class GrandFather{
    void think(){
        System.out.println("GrandFather");
    }
}
class Father extends GrandFather{
    void think(){
        System.out.println("Father");
    }
}

