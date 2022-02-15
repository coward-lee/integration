package design.parttern.decorator;

public class Coffee extends Drink{
    @Override
    float cost() {
        return getPrice();
    }
}
