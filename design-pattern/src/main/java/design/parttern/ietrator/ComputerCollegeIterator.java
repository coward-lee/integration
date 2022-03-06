package design.parttern.ietrator;

public class ComputerCollegeIterator implements Iterator {
    // 之类我们需要知道是以怎样的方式存放
    Department[] departments;

    int position = 0;

    public ComputerCollegeIterator(Department[] departments) {
        this.departments = departments;
    }

    @Override
    public boolean hasNext() {
        return position < departments.length - 1;
    }

    @Override
    public Object next() {
        return departments[++position];
    }
}
