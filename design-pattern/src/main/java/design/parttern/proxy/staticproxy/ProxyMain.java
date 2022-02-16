package design.parttern.proxy.staticproxy;

public class ProxyMain {
    public static void main(String[] args) {
        StaticProxyInterface proxiee = new StaticProxiee();
        StaticProxyInterface proxy = new StaticProxy(proxiee);
        proxy.doSomething();
    }
}
