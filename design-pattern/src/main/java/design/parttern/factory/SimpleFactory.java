package design.parttern.factory;

import design.parttern.pojo.Product;

/**
 * 简单工厂模式
 * 直接返回需要的对象
 */
public class SimpleFactory {
    public static Product createFruit(){
        return new Product("fruit");
    }
}
