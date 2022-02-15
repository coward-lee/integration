package design.parttern.decorator;

public class Coffe extends Drink{
    @Override
    float cost() {
        return getPrice();
    }
}
