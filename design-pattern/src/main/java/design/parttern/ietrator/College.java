package design.parttern.ietrator;

public interface College {

    public String getName();

    public void addDepartment(Department department);

    public Iterator iterator();
}
