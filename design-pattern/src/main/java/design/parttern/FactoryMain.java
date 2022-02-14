package design.parttern;

import design.parttern.factory.AbstractFactory;
import design.parttern.factory.FactoryMethod.*;
import design.parttern.factory.SimpleFactory;

public class FactoryMain {
    public static void main(String[] args) {
        // 简单工厂模式
        System.out.println(SimpleFactory.createFruit());
        // 工厂方法模式
        CarFactory teslaFactor = new TeslaFactor();
        System.out.println(teslaFactor.getCar());
        CarFactory wulinFactor = new WulinFactor();
        System.out.println(wulinFactor.getCar());
        // 抽象工厂模式
        System.out.println("抽象工厂模式");
        new AbstractFactory.CreateCar(new AbstractFactory.TeslaFactory());
    }
}
