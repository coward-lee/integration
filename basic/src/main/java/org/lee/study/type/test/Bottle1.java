package org.lee.study.type.test;

class Bottle1<T extends Juice> {
    private T drink;

    public Bottle1(T drink) {
        drink = drink;
    }

    public T getDrink() {
        return drink;
    }

    public void setDrink(T drink) {
        drink = drink;
    }
}
