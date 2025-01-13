package exceptions;

public class ThrowableDemo {
    // error 的测试
    private void error(){
        throw new Error("????");
    }
    // error 的try-catch
    private void errorTryCatch (){
        try{
            throw new Error("");
        }catch (Error e){
            e.printStackTrace();
        }
    }
    // error 的 throws
    private void errorThrows()  throws Error {
            throw new Error("");
    }

    // exception 直接 throws
    private void exceptionThrows() throws Exception {
        throw new Exception("????");
    }
    // exception 直接 try-catch
    private void exceptionTryCatch() {
        try {
            throw new Exception("????");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runtimeException(){
        throw new RuntimeException("????");
    }

    private void runtimeExceptionThrows() throws RuntimeException {
        throw new RuntimeException("????");
    }

    private void runtimeExceptionTryCatch(){
        try {
            throw new RuntimeException("????");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void throwable() throws Throwable {
        throw new Throwable("????");
    }
    private void errorDemo() {
        throw new NoClassDefFoundError();
    }

    public static void main(String[] args) {
        int i = 1/0;
        System.out.println(i);
    }



}
