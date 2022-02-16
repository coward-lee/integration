package design.parttern.decorator;
// 装饰器的实现,就是咖啡的调味品
public class Milk extends Decorator {
    public Milk(Drink drink) {
        super(drink);
        setDesc("牛奶");
        setPrice(2f);
    }
    
}
