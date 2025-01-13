package design.parttern.strategy;

public abstract class Duck {
    FlyBehavior flyBehavior;

    public Duck() {
    }

    public Duck(FlyBehavior flyBehavior) {
        this.flyBehavior = flyBehavior;
    }

    public abstract void display();

    public void quack(){
        System.out.println("鸭子呱呱叫");
    }
    public void swim(){
        System.out.println("会游泳");
    }
    public void fly(){
        if (flyBehavior!=null){
            flyBehavior.fly();
        }
    }
}
