package design.parttern.singleton;

public class InnerClass {

    private InnerClass(){}


    public static InnerClass getInstance(){
        return InstanceHolder.INNER_CLASS;
    }

    private static class InstanceHolder{
        private final static InnerClass INNER_CLASS = new InnerClass();
    }
}
