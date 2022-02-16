package design.parttern.decorator;

/**
 * 装饰着模式
 */
public class DecoratorMain {
    public static void main(String[] args) {

        Drink order = new LongBlack();
        System.out.println("费用1:"+order.getPrice());
        System.out.println("描述:"+order.getDesc());

        order = new Milk(order);
        System.out.println("加了牛奶的价格:"+order.cost());
        System.out.println("加了牛奶:"+order.getDesc());


        order = new Chocolate(order);
        System.out.println("加了巧克力的价格:"+order.cost());
        System.out.println("加了巧克力:"+order.getDesc());
    }
}
