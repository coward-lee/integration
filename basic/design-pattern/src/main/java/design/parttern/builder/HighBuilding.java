package design.parttern.builder;

public class HighBuilding extends HouseBuilder{
    @Override
    public void buildBasic() {
        System.out.println("高房子打地基10000cm");
    }

    @Override
    public void buildWall() {
        System.out.println("高房子砌墙10000cm");
    }

    @Override
    public void roofed() {
        System.out.println("高房子盖屋顶");
    }
}
