package design.parttern.adapter;
/**
 * 接口适配器模式:
 * 当不需要全部实现接口提供的方法时，可先设计一个抽象类实现接口，并为该接口中每个方法提供一个默认实现（空方法)，那么该抽象类的子类可有选择地覆盖父类的某些方法来实现需求
 * 适用于一个接口不想使用其所有的方法的情况。
 */
public abstract class DefaultAdapterPattern {
    public void adapter01(){

    }
    public void adapter02(){

    }
    public void adapter03(){

    }
    // 整个是随便写的为了利用起来前面三个方法.
    public void mainProcess(){
        adapter01();
        adapter02();
        adapter03();
    }
}
class Impl extends DefaultAdapterPattern{
    @Override
    public void adapter01() {
        System.out.println("实现了一个方法");
    }
}
