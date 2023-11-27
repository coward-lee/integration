package org.lee.guava.utilities;

import com.google.errorprone.annotations.concurrent.GuardedBy;

public class GuardedByDemo {

    @GuardedBy("this")
    int i = 0;
    void ii(){
        synchronized (this){
            i++;
        }
    }
    void noIi(){
        i++;
    }
}
