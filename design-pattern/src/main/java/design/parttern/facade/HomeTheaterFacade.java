package design.parttern.facade;


public class HomeTheaterFacade {
    // 定义各个子系统
    private TheaterLight theaterLight;
    private DVDPlayer dvdPlayer;
    private PopCorn popCorn;
    private Projector projector;
    private Screen screen;
    private Stereo stereo;

    public HomeTheaterFacade() {
        super();
        this.theaterLight = TheaterLight.getInstance();
        this.dvdPlayer =    DVDPlayer.getInstance();
        this.popCorn =      PopCorn.getInstance();
        this.projector =    Projector.getInstance();
        this.screen =       Screen.getInstance();
        this.stereo =       Stereo.getInstance();
    }
    public void open(){
        popCorn.on();
        popCorn.pop();
        projector.on();
        stereo.on();
        dvdPlayer.on();
        theaterLight.dim();
    }
    public void play(){
        dvdPlayer.play();
    }

    public void pause(){
        dvdPlayer.pause();
    }

    public void end(){
        popCorn.off();
        theaterLight.brighter();
        screen.up();
        projector.off();
        stereo.off();
        dvdPlayer.off();
    }
}
