package design.parttern.adapter;

public class Phone {
    public void charge(Voltage5V voltage5V){
        int v = voltage5V.outputV5();
        if (v == 5){
            System.out.println("电压为5v，可以充电。");
        }
    }
}
