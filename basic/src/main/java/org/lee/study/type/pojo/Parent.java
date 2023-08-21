package org.lee.study.type.pojo;

import java.util.Objects;

public class Parent {

    private int code;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parent parent = (Parent) o;
        return code == parent.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
