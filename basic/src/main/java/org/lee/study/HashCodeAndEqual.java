package org.lee.study;

import java.util.HashSet;
import java.util.Objects;

public class HashCodeAndEqual {
    String name;
    int age;

    public HashCodeAndEqual(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "HashCodeAndEqual{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashCodeAndEqual that = (HashCodeAndEqual) o;
        return age == that.age && Objects.equals(name, that.name);
    }

    public static void main(String[] args) {
        HashSet<HashCodeAndEqual> sets = new HashSet<>();
        sets.add(new HashCodeAndEqual("name",1));
        sets.add(new HashCodeAndEqual("name",1));
        sets.forEach(System.out::println);
    }
}
