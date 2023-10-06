//package test.action.jdk21;
//
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//class VirtualThreadTest {
//
//    private final Logger log = LoggerFactory.getLogger(VirtualThreadTest.class);
//    @Test
//    void test(){
//        System.out.println("virtual thread test");
////
//        Thread start = Thread.ofVirtual().name("deom").start(new Runnable() {
//            @Override
//            public void run() {
//                log.info("test");
//            }
//        });
//        System.out.println(start);
//    }
//
//    public static void main(String[] args) {
//        System.out.println("virtual thread test");
//
//    }
//
//}
