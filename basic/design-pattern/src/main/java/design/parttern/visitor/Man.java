package design.parttern.visitor;

/**
 * 1. 这里使用了一个双分派，首先在客户端中吗，将具体状态作为参数传递Woman中（第一次分派）
 * 2. 然后将woman 类作为参数的具体方法中方法getWomanResult,同时将自己（this）作为参数传入，完成第二次分派
 */
public class Man extends Person{
    @Override
    public void accept(Action action) {
        action.getManResult(this);
    }
}
