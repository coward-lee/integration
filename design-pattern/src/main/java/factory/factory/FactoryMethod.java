package factory.factory;

import factory.pojo.Car;

/**
 * 满足开闭原则的工厂方法模式
 * 多一个车就需要多加一个工厂实现对应的工厂接口就行了
 * 就是把主流程写好，子工厂实现对应的单个流程就行了
 */
public class FactoryMethod {
    public static abstract class CarFactory{
        abstract Car createCar();
        public Car getCar(){
            System.out.println("prepare car");
            return createCar();
        }
    }


    public static class TeslaFactor extends CarFactory{
        @Override
        public Car createCar() {
            return new Car("Tesla", 999);
        }
    }
    public static class WulinFactor extends CarFactory{
        @Override
        public Car createCar() {
            return new Car("Wulin", 999);
        }
    }
}


