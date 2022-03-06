package design.parttern.template.improve;

public class PureSoyaMilk extends SonyMilk {
    @Override
    void addition() {
        System.out.println("第二步: 添加了花生");
    }

    @Override
    boolean customWantAdd() {
        return true;
    }
}
