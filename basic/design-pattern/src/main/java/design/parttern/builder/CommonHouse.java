package design.parttern.builder;

public class CommonHouse extends HouseBuilder{
    @Override
    public void buildBasic() {
        System.out.println("普通房子打地基10cm");
    }

    @Override
    public void buildWall() {
        System.out.println("普通房子砌墙10cm");
    }

    @Override
    public void roofed() {
        System.out.println("普通房子盖屋顶");
    }
}
