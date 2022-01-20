package factory.factory;

import factory.pojo.Car;

/**
 * 抽象工厂模式
 */
public class AbstractFactory {
    public static interface AbsFactory{
        public Car create(Integer price);
    }
    public static class TeslaFactory implements AbsFactory {
        @Override
        public Car create(Integer price) {
            return new Car("Tesla", 999);
        }
    }
    public static class WulinFactory implements AbsFactory {
        @Override
        public Car create(Integer price) {
            return new Car("Wulin", 999);
        }
    }

    public static class CreateCar{
        public CreateCar(AbsFactory absFactory) {
            setAbsFactory(absFactory);
        }

        AbsFactory absFactory;

        public void setAbsFactory(AbsFactory absFactory) {
            System.out.println("prepare to create car");
            this.absFactory = absFactory;
            System.out.println(absFactory.create(999));
        }
    }

}
