package design.parttern.bridge;

public class UpRightPhone  extends Phone {

    // 传递构造器
    public UpRightPhone(Brand brand) {
        super(brand);
    }

    @Override
    protected void open() {
        System.out.println("直板手机");
        super.open();
    }

    @Override
    protected void call() {
        System.out.println("直板手机");
        super.call();
    }

    @Override
    protected void close() {
        System.out.println("直板手机");
        super.close();
    }
}