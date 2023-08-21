import java.util.concurrent.locks.ReentrantLock;

public class Main1 {

    public static void main(String[] args) {

        String s3 = "world" + new String("hel");
//        String s2 = new String("hel") + " world";
//        String s1 = "hello";
//        String s2 = "hello"+"hel";
//        String s5 = new String("hello");
//
//        System.out.println(s1 == s2);
//        LocalDate.now();
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor();
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();
    }


}

