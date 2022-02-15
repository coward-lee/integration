package design.parttern.adapter;

/**
 * 类适配器：
 * 缺点适配内容多了以后，适配方法也会增加
 */
public class VoltageAdapter extends Voltage220V implements Voltage5V {
    public int outputFive(){
        System.out.println("220V转化为5V");
        return 5;
    }
}
