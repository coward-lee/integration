package design.parttern.ietrator;

import java.util.Arrays;

public class ComputerCollege implements College {
    Department[] departments;
    int index = 0;

    public ComputerCollege() {
        this.departments = new Department[100];
        addDepartment(new Department("java 专业", "java 专业"));
        addDepartment(new Department("php 专业", "php 专业"));
        addDepartment(new Department("大数据 专业", "大数据 专业"));

    }

    @Override
    public String getName() {
        return "计算机学院";
    }

    @Override
    public void addDepartment(Department department) {
        departments[index++] = department;
    }

    @Override
    public Iterator iterator() {
        return new ComputerCollegeIterator(departments);
    }
}
