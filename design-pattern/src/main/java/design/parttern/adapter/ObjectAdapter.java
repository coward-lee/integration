package design.parttern.adapter;

import java.util.Objects;

/**
 * 对象适配器：
 * 他不是实现适配的类，而是依赖适配的类.
 */
public class ObjectAdapter implements Voltage5V {
    private Voltage220V voltage220V;

    public ObjectAdapter(Voltage220V voltage220V) {
        this.voltage220V = voltage220V;
    }


    @Override
    public int outputV5() {
        if (Objects.nonNull(voltage220V)){
            int src = voltage220V.output();
            System.out.println("原来的电压："+src);
            System.out.println("变压后的电压："+5);
        }
        return 5;
    }
}
