package org.lee.study.memory;

public class GCDemo {
    public static void main(String[] args) {

    }
}

class GcObject{
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
