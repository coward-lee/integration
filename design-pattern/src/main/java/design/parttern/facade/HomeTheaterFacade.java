package design.parttern.facade;

import com.sun.org.apache.bcel.internal.generic.POP;

public class HomeTheaterFacade {
    // 定义各个子系统
    private TheaterLight theaterLight;
    private DVDPlayer dvdPlayer;
    private PopCorn popCorn;
    private Projector projector;
    private Screen screen;
    private Stereo stereo;

    public HomeTheaterFacade(TheaterLight theaterLight,
                             DVDPlayer dvdPlayer,
                             PopCorn popCorn,
                             Projector projector,
                             Screen screen,
                             Stereo stereo
    ) {
        super();
        this.theaterLight = TheaterLight.getInstance();
        this.dvdPlayer =    DVDPlayer.getInstance();
        this.popCorn =      PopCorn.getInstance();
        this.projector =    Projector.getInstance();
        this.screen =       Screen.getInstance();
        this.stereo =       Stereo.getInstance();
    }
    public void read(){
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
