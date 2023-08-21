package org.lee.study.type.test;

class Bottle<T> {
    private T drink;

    public Bottle(T drink) {
        drink = drink;
    }

    public T getDrink() {
        return drink;
    }

    public void setDrink(T drink) {
        drink = drink;
    }
}
