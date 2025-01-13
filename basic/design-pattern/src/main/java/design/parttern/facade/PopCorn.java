package design.parttern.facade;

public class PopCorn {
    private final static PopCorn instance = new PopCorn();

    public static PopCorn getInstance() {
        return instance;
    }
    public void on(){
        System.out.println("PopCorn on");
    }

    public void off(){
        System.out.println("PopCorn off");
    }

    public void pop(){
        System.out.println("PopCorn pop");
    }

}
