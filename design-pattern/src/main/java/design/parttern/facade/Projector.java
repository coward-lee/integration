package design.parttern.facade;

public class Projector {
    private final static Projector instance = new Projector();

    public static Projector getInstance() {
        return instance;
    }
    public void on(){
        System.out.println("Projector on");
    }

    public void off(){
        System.out.println("Projector off");
    }

    public void focus(){
        System.out.println("Projector focus");
    }

}
