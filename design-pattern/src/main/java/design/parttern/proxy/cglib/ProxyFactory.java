package design.parttern.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class ProxyFactory implements MethodInterceptor {

    private Object target;

    public ProxyFactory(Object target) {
        this.target = target;
    }

    public Object getProxyInstance(){
        // 1.  创建工具类
        Enhancer enhancer = new Enhancer();
        // 2. 设置父类
        enhancer.setSuperclass(target.getClass());
        // 3. 设置回调函数
        enhancer.setCallback(this);
        return enhancer.create();
    }

    /**
     *
     * @param obj       被代理的对象
     * @param method    需要执行的方法
     * @param args      参数
     * @param proxy     used to invoke super (non-intercepted method); may be called as many times as needed
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

        System.out.println("cglib 动态代理开始了");

        Object invoke = method.invoke(target, args);

        System.out.println("cglib 动态代理结束了");

        System.out.println("cglib proxy 动态代理开始了");
// 回去调用父类,但
        Object invoke1 = proxy.invoke(obj, args);

        System.out.println("cglib proxy 动态代理开始了");

        return invoke;
    }
}
