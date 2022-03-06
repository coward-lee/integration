package design.parttern.proxy.dynamic;

public class DynamicMain {
    public static void main(String[] args) {
        ProxyInterface target = new OriginalClass();

        ProxyInterface proxy = (ProxyInterface)(new ProxyFactory(target).getProxy());
        System.out.println(proxy);
        System.out.println(proxy.getClass());
        proxy.doSomething();
    }
}
