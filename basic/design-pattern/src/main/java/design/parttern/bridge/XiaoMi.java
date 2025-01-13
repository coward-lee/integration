package design.parttern.bridge;

public class XiaoMi implements Brand{
    @Override
    public void open() {
        System.out.println("xiaomi open");
    }

    @Override
    public void call() {

        System.out.println("xiaomi call");
    }

    @Override
    public void close() {

        System.out.println("xiaomi close");
    }
}
