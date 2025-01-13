package design.parttern.visitor;

public class Fail extends Action {
    @Override
    public void getManResult(Man man) {

        System.out.println("男性评价该歌手很不成功");
    }

    @Override
    public void getWomanResult(Woman woman) {

        System.out.println("女性评价该歌手很不成功");
    }
}
