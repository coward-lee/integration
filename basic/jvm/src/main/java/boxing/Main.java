package boxing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1,2,3,4);
        Integer i2 = 2;
        Integer i1 = 2;
        System.out.println(i1==i2);
        System.out.println(i1.equals(i2));
        if (true){
            System.out.println("xxx");
        }else{
            System.out.println("√√√");
        }
    }
}
