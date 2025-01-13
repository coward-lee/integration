package error;


import java.util.LinkedList;
import java.util.List;

public class OOMTest {
    public static void main(String[] args) {
        List<Object> objects = new LinkedList<>();
        while(true){
            System.out.println("???????????????????");
            objects.add(new Object());
        }
    }
}
