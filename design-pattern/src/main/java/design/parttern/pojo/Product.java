package design.parttern.pojo;

/**
 * 需要产生的对象
 */
public class Product {
    String name;

    public Product(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                '}';
    }
}
