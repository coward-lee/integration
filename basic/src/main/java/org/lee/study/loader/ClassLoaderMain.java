package org.lee.study.loader;

public class ClassLoaderMain {
    public static void main(String[] args) throws Throwable {
        System.out.println("1 + 2 == " + fun());
//        Thread.currentThread().getContextClassLoader()
    }


    static int fun(){
        int m;
        int n;
        m = 1;
        n = 2;
        int r = m + n;
        return r;
    }
}
