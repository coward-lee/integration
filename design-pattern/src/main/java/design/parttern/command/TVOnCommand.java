package design.parttern.command;

public class TVOnCommand implements Command{
    TVReceiver receiver;


    public TVOnCommand(TVReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.on();
    }

    @Override
    public void undo() {
        receiver.off();
    }
}

