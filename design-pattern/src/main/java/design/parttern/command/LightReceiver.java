package design.parttern.command;

public class LightReceiver {
    public void on(){
        System.out.println("接收到了 开启命令");
    }
    public void off(){
        System.out.println("接收到了 关闭命令");
    }
}
