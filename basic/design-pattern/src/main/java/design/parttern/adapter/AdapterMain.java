package design.parttern.adapter;

/**
 * 适配器模式
 */
public class AdapterMain {
    public static void main(String[] args) {
        // 类适配器模式
        System.out.println("=========== 类适配器模式 ==========");
        Phone phone = new Phone();
        phone.charge(new VoltageAdapter());
        // 对象适配器
        System.out.println("========== 对象适配器 ==========");
        phone.charge(new ObjectAdapter(new Voltage220V()));
    }
}
