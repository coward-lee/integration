package design.parttern.ietrator;

import java.util.ArrayList;
import java.util.List;

public class InfoCollege implements College {
    List<Department> departments;
    int index = 0;

    public InfoCollege() {
        this.departments = new ArrayList<>(100);
        addDepartment(new Department("电子信息 专业", "电子信息 专业"));
        addDepartment(new Department("数据科学 专业", "数据科学 专业"));
        addDepartment(new Department("小数据 专业", "小数据 专业"));

    }

    @Override
    public String getName() {
        return "信息学院";
    }

    @Override
    public void addDepartment(Department department) {
        departments.add(department);
    }

    @Override
    public Iterator iterator() {
        return new InfoCollegeIterator(departments);
    }
}
