package design.parttern.facade;

public class DVDPlayer {
    private final static DVDPlayer instance = new DVDPlayer();

    public static DVDPlayer getInstance() {
        return instance;
    }
    public void on(){
        System.out.println("ddvd on");
    }

    public void off(){
        System.out.println("dvd off");
    }

    public void play(){
        System.out.println("dvd play");
    }

    public void pause(){
        System.out.println("dvd pause");
    }
}
