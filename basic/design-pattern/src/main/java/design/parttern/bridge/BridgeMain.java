package design.parttern.bridge;

/**
 * 桥接模式
 * 在此例中就相当于 phone 的抽象类起到了桥接的作用 Brand和对应的实体手机的作用,
 * 最后在实例化的时候进行聚合就行了
 */
public class BridgeMain {
    public static void main(String[] args) {
        Phone phone = new FoldedPhone(new XiaoMi());
        phone.open();
        phone.call();
        phone.close();
        phone = new UpRightPhone(new Vivo());
        phone.open();
        phone.call();
        phone.close();
    }
}
