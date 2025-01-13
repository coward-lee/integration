package design.parttern.singleton;

public class DoubleCheck {

    private static volatile   DoubleCheck  ins= null;

    private DoubleCheck(){}


    public static DoubleCheck getIns() {
        if (ins == null){
            synchronized (DoubleCheck.class){
                if (ins == null){
                    ins = new DoubleCheck();
                }
            }
        }
        return ins;
    }
}
