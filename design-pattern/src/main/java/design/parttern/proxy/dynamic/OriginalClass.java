package design.parttern.proxy.dynamic;

public class OriginalClass implements ProxyInterface{
    @Override
    public void doSomething() {
        System.out.println("实际执行的动作");
    }

    public void doSomething1() {
        System.out.println("实际执行的动作,没有实现接口的方法");
    }

}
