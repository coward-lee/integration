package design.parttern.ietrator;

import java.util.ArrayList;

public class IteratorMain {
    public static void main(String[] args) {
        ArrayList<College> colleges = new ArrayList<>();
        ComputerCollege computerCollege = new ComputerCollege();
        InfoCollege infoCollege = new InfoCollege();
        colleges.add(computerCollege);
        colleges.add(infoCollege);
        OutputImpl output = new OutputImpl(colleges);
        output.printCollege();
    }
}
