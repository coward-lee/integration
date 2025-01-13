package design.parttern.visitor;

public abstract class Action {
    //  获得男性的评价
    public abstract void getManResult(Man man);
    // 活儿都女性的评价

    public abstract void getWomanResult(Woman woman);
}
