package design.parttern.command;

/**
 * 没有命令, 主要用于初始化的时候使用,
 * 这是一种设计模式,可以省去对空的判断。
 */
public class NoCommand implements Command{
    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }
}
