package design.parttern.decorator;

public class Decorator extends Drink {
    private final Drink drink;

    public Decorator(Drink drink) {
        this.drink = drink;
    }

    @Override
    float cost() {
        return super.getPrice() + drink.cost();
    }

    @Override
    public String toString() {
        return "Decorator{" +
                "drink=" + drink +
                ", desc='" + desc + '\'' +
                '}';
    }

    @Override
    public String getDesc() {
        return super.getDesc()+ " " + drink.getDesc()+ " " + getPrice();
    }
}
