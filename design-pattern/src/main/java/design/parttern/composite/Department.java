package design.parttern.composite;

public class Department extends OrganizationComponent {
    public Department(String name, String desc) {
        super(name, desc);
    }

    @Override
    public void add(OrganizationComponent organizationComponent) {
        super.add(organizationComponent);
    }

    // 由于department以及是叶子节点了,所以不听过remove方法
//    @Override
//    public void remove(OrganizationComponent organizationComponent) {
//        super.remove(organizationComponent);
//    }

    @Override
    protected void print() {
        System.out.println(getName());
    }
}
