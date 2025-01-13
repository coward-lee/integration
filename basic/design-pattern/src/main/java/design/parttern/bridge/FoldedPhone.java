package design.parttern.bridge;

public class FoldedPhone extends Phone {

    // 传递构造器
    public FoldedPhone(Brand brand) {
        super(brand);
    }

    @Override
    protected void open() {
        System.out.println("折叠手机");
        super.open();
    }

    @Override
    protected void call() {
        System.out.println("折叠手机");
        super.call();
    }

    @Override
    protected void close() {
        System.out.println("折叠手机");
        super.close();
    }
}
