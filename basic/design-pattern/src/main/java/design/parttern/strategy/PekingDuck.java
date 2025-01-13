package design.parttern.strategy;

public class PekingDuck extends Duck{

    public PekingDuck() {
        flyBehavior=new BadFlyBehavior();
    }

    @Override
    public void display() {
        System.out.println("北京鸭北京鸭北京鸭北京鸭");
    }

    @Override
    public void fly() {
        System.out.println("北京鸭不能飞");
    }
}
