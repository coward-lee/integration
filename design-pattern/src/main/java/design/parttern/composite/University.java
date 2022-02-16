package design.parttern.composite;

import java.util.ArrayList;
import java.util.List;

public class University extends OrganizationComponent{
    List<OrganizationComponent> components = new ArrayList<>();

    public University(String name, String desc) {
        super(name, desc);
    }

    @Override
    public void add(OrganizationComponent organizationComponent) {
        components.add(organizationComponent);
    }

    @Override
    public void remove(OrganizationComponent organizationComponent) {
        components.remove(organizationComponent);
    }

    @Override
    protected void print() {
        System.out.println("==============="+getName()+"====================");
        components.forEach(OrganizationComponent::print);
    }
}
