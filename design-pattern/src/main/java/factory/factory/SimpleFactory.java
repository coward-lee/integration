package factory.factory;

import factory.pojo.Product;

/**
 * 简单工厂模式
 * 直接返回需要的对象
 */
public class SimpleFactory {
    public static Product createFruit(){
        return new Product("fruit");
    }
}
