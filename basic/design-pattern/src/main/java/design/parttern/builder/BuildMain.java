package design.parttern.builder;

public class BuildMain {
    public static void main(String[] args) {
        // 该普通房子
        CommonHouse commonHouse = new CommonHouse();
        // 准备创建房子的指挥者
        HouseDirector houseDirector = new HouseDirector(commonHouse);
        System.out.println("输出流程");
        // 完成盖房子的流程
        House house = houseDirector.constructHouse();
        System.out.println(house);
        HighBuilding highBuilding = new HighBuilding();
        houseDirector.setHouseBuilder(highBuilding);
        House house1 = houseDirector.constructHouse();
        System.out.println(house1);
    }
}
