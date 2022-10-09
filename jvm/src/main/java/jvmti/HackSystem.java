//package jvmti;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.PrintStream;
//
//public class HackSystem {
//    public static final InputStream in = System.in;
//    private static ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//    public static final PrintStream out = new PrintStream(buffer);
//    public static final PrintStream err = out;
//
//    public static String getBufferString(){
//        return buffer.toString();
//    }
//
//    public static void setSecurity(final SecurityManager sm){
//        System.setSecurityManager(sm);
//    }
//    public static SecurityManager getSecurityManager(){
//        return System.getSecurityManager();
//    }
//    public static long currentTimeMills(){
//        return System.currentTimeMillis();
//    }
//
//    public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length){
//        System.arraycopy(src, srcPos, dest, destPos, length);
//    }
//
//    public static int identifyHashCode(Object x){
//        return System.identityHashCode(x);
//    }
//    public static void clearBuffer(){
//        try {
//            buffer.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
