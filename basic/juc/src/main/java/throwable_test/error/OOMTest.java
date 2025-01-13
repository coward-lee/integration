package throwable_test.error;

import basic.create_thread.ThreadPoolDemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class OOMTest {
    public static void main(String[] args) {
        List<Object> objects = new LinkedList<>();
        while(true){
            System.out.println("???????????????????");
            objects.add(new ThreadPoolDemo());
        }
    }
}
