package design.parttern.composite;

/**
 * 组合模式
 */
public class Composite {
    public static void main(String[] args) {
        System.out.println();

        OrganizationComponent university = new University("清华","清华");
        // 创建学院
        OrganizationComponent college1 = new College("计算机学院","计算机学院");
        OrganizationComponent college2 = new College("电子信息","电子信息");
        college1.add(new Department("软件工程", "software"));
        college1.add(new Department("cs", "计算机"));
        college1.add(new Department("网络工程", "cn"));

        college2.add(new Department("通信工程", "通信"));
        college2.add(new Department("信息工程", "信息"));
        university.add(college1);
        university.add(college2);
        university.print();
    }
}
