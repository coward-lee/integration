package design.parttern.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

public class ProxyFactory {
    private Object target;

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public ProxyFactory(Object target) {
        this.target = target;
    }
    public Object getProxy(){
        /**
         *  Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h)
         *  1. ClassLoader loader : 指定当前目标对象的类加载器,获取累加其的方法固定
         *  2. Class<?> interfaces : 目标对象实现的接口类型,使用泛型确认类型
         *  3.  InvocationHandler h : 事件处理,执行目标方法时,会触发事件处理器方法,会把当前执行的目标对象方法作为参数传入
         */
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("jdk 动态代理开始");
                        Object invoke = method.invoke(target, args);
                        System.out.println("jdk 动态代理结束准备返回值了");
                        return invoke;
                    }
                }
        );
    }
}
