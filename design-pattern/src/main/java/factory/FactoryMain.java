package factory;

import factory.factory.AbstractFactory;
import factory.factory.FactoryMethod.*;
import factory.factory.SimpleFactory;

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
