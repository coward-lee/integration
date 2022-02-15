package design.parttern.decorator;
// 装饰器的实现,就是咖啡的调味品
public class Chocolate extends Decorator {
    public Chocolate(Drink drink) {
        super(drink);
    }
    
}
