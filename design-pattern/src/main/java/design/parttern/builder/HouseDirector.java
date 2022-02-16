package design.parttern.builder;

public class HouseDirector {
    HouseBuilder houseBuilder = null;

    // 传入具体的 houseBuilder
    public HouseDirector(HouseBuilder houseBuilder) {
        this.houseBuilder = houseBuilder;
    }

    // 传入具体的 houseBuilder
    public void setHouseBuilder(HouseBuilder houseBuilder) {
        this.houseBuilder = houseBuilder;
    }

    // 将如何处理建造房子的流程，交给指挥者

    public House constructHouse(){
        houseBuilder.buildBasic();
        houseBuilder.buildWall();
        houseBuilder.roofed();
        return houseBuilder.houseBuild();
    }
}
