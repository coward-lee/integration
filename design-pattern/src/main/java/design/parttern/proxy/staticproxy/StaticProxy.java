package design.parttern.proxy.staticproxy;

public class StaticProxy implements StaticProxyInterface{
    StaticProxyInterface staticProxyInterface;

    public StaticProxy(StaticProxyInterface staticProxyInterface) {
        this.staticProxyInterface = staticProxyInterface;
    }

    @Override
    public void doSomething() {
        System.out.println("  StaticProxy    ");
        staticProxyInterface.doSomething();
    }
}
