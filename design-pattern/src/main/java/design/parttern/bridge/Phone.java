package design.parttern.brige;

public abstract class Phone {

    private final Brand brand;

    public Phone(Brand brand) {
        super();
        this.brand = brand;
    }


    protected void open() {
        brand.open();
    }

    protected void call() {
        brand.call();
    }

    protected void close() {
        brand.close();
    }
}
