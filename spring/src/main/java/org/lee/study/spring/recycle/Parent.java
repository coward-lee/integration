package org.lee.study.spring.recycle;

public class Parent<T> {
    T tt;
    public T get(T t){
        tt = t;
        return t;
    }
}
