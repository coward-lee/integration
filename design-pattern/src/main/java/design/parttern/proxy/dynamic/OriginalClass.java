package design.parttern.proxy.dynamic;

public class OriginalClass implements ProxyInterface{
    @Override
    public void doSomething() {
        System.out.println("实际执行的动作");
    }
}
