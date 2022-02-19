package design.parttern.ietrator;

import java.util.List;

public class InfoCollegeIterator implements Iterator {
    // 之类我们需要知道是以怎样的方式存放
    List<Department> departments;

    int position = 0;

    public InfoCollegeIterator(List<Department> departments) {
        this.departments = departments;
    }

    @Override
    public boolean hasNext() {
        return position < departments.size() - 1;
    }

    @Override
    public Object next() {
        return departments.get(++position);
    }
}
