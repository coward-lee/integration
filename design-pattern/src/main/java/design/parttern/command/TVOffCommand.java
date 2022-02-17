package design.parttern.command;

public class TVOffCommand implements Command{
    TVReceiver receiver;


    public TVOffCommand(TVReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.off();
    }

    @Override
    public void undo() {
        receiver.on();
    }
}
