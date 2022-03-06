package design.parttern.strategy;

public class BadFlyBehavior implements FlyBehavior
{
    @Override
    public void fly() {
        System.out.println("飞翔技术不好的鸭子");
    }
}
