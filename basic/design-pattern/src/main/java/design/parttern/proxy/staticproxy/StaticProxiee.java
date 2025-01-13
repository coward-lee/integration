package design.parttern.proxy.staticproxy;

/**
 * 被代理的对象
 */
public class StaticProxiee implements StaticProxyInterface{
    @Override
    public void doSomething() {
        System.out.println("StaticProxiee");
    }
}
