package design.parttern.strategy;

public class GoodFlyBehavior implements FlyBehavior{
    @Override
    public void fly() {
        System.out.println("飞翔技术好的鸭子");
    }
}
