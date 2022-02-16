package design.parttern.facade;

public class TheaterLight {
    private final static TheaterLight instance = new TheaterLight();

    public static TheaterLight getInstance() {
        return instance;
    }
    public void on(){
        System.out.println("TheaterLight on");
    }

    public void off(){
        System.out.println("TheaterLight off");
    }

    public void brighter(){
        System.out.println("TheaterLight brighter");
    }

    public void dim(){
        System.out.println("TheaterLight dim");
    }
}
