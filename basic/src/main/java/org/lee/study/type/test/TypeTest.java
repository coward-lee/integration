package org.lee.study.type.test;

public class TypeTest {

    /**
     * 上界通配符
     */
    static void upThreshold(){
        Bottle<? extends Juice> bottle = new Bottle<>(new AppleJuice());
        Bottle<? super Juice> bottle1 = new Bottle<>(new Drink());

    }

    public static void main(String[] args) {

    }
}


