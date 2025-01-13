package design.parttern.command;

public class CommandController {
    Command[] onCommands;
    Command[] offCommands;

    Command undoCommand;

    public CommandController() {
        onCommands = new Command[5];
        offCommands = new Command[5];
        for (int i = 0; i < 5; i++) {
            onCommands[i] = new NoCommand();
            offCommands[i] = new NoCommand();
        }
    }

    public void setCommand(int on, Command onCommand, Command offCommand){
        onCommands[on] = onCommand;
        offCommands[on] = offCommand;
    }

    // 按下按钮
    public void onButtonWasPush(int on){
        // 找到你按下的开按钮，并调用对应的方法
        onCommands[on].execute();
        undoCommand = offCommands[on];
    }
    // 按下按钮
    public void offButtonWasPush(int off){
        // 找到你按下的开按钮，并调用对应的方法
        offCommands[off].execute();
        undoCommand = onCommands[off];
    }

    public void undoButtonWasPushed(){
        undoCommand.execute();
    }
}
