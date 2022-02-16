package design.parttern.decorator;

public abstract class Drink {
    public String desc;
    private float price = 0f;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    abstract float cost();

    @Override
    public String toString() {
        return "Drink{" +
                "desc='" + desc + '\'' +
                ", price=" + price +
                '}';
    }
}
