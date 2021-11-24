package extend;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

public class Son extends Father{
    void think(){
        try {
            MethodType mt = MethodType.methodType(void.class);
//          MethodHandle mh = MethodHandles.lookup().findSpecial(GrandFather.class, "think", mt, getClass());
//          mh.invoke(this);
            Field lookUp = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            lookUp.setAccessible(true);
            MethodHandle mh = ((MethodHandles.Lookup) lookUp.get(null))
                    .findSpecial(GrandFather.class, "think", mt, GrandFather.class);
            mh.invoke(this);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}