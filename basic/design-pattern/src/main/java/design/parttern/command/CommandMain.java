package design.parttern.command;

public class CommandMain {
    public static void main(String[] args) {
        LightReceiver lightReceiver = new LightReceiver();

        Command lightOnCommand = new LightOnCommand(lightReceiver);
        Command lightOffCommand = new LightOffCommand(lightReceiver);

        CommandController commandController = new CommandController();

        commandController.setCommand(0, lightOnCommand, lightOffCommand);

        System.out.println("======================按下灯的开按钮========================");
        commandController.onButtonWasPush(0);
        commandController.offButtonWasPush(0);
        commandController.undoButtonWasPushed();

        System.out.println("=====================控制电视====================");
        TVReceiver tvReceiver = new TVReceiver();
        commandController.setCommand(1, new TVOnCommand(tvReceiver), new TVOffCommand(tvReceiver));

        commandController.onButtonWasPush(1);
        commandController.offButtonWasPush(1);
        commandController.undoButtonWasPushed();
    }
}
