package test.action.juc;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.ReentrantLock;

public class AQSDemo {

    @Test
    void unlock_first_demo(){
        ReentrantLock lok = new ReentrantLock();
        lok.unlock();
    }
}
