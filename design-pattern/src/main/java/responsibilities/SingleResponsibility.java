package responsibilities;

/**
 * 单一职责原则
 */
public class SingleResponsibility {

    public static void main(String[] args) {
        // 方案一： 直接一个类，这样不灵活，而且不符合单一职责原则
        new Vehicle().run();
        // 方案二： 多个类，这样造成了类分解，同时修改了客户端
        new RoadVehicle("汽车").run();
        new AirVehicle("飞机").run();
        // 方案三：多个方法
        Vehicle2 vehicle2 = new Vehicle2();
        vehicle2.runAir("飞机");
        vehicle2.runRoad("car");
        vehicle2.runWater("boat");
    }

    // 方案一： 直接一个类，这样不灵活，而且不符合单一职责原则
    static class Vehicle{
         void run(){
             System.out.println("在运行");
         }
    }
    // 方案二： 多个类，这样造成了类分解，同时修改了客户端
    static class RoadVehicle{
        String name;

        public RoadVehicle(String name) {
            this.name = name;
        }

        void run() {
            System.out.println("在路上跑："+name);
        }
    }

    static class AirVehicle {
        String name;

        public AirVehicle(String name) {
            this.name = name;
        }
        void run() {
            System.out.println("在天上飞："+name);
        }
    }
    // 方案三：//
    static class Vehicle2{
        void runRoad(String vehicle){
            System.out.println("公路上跑的："+vehicle);
        }
        void runAir(String vehicle){
            System.out.println("空中飞："+vehicle);
        }
        void runWater(String vehicle){
            System.out.println("水中游："+vehicle);
        }
    }
}
