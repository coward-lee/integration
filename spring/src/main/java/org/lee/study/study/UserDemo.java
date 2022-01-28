package org.lee.study.study;

public class UserDemo {
    String name;
    int id;

    public UserDemo() {
    }

    public UserDemo(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
