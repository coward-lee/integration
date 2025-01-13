package design.parttern.ietrator;

import java.util.List;

public class OutputImpl {
    // 学院集合
    List<College> collegeList;

    public OutputImpl(List<College> collegeList) {
        this.collegeList = collegeList;
    }

    public void printCollege(){
        java.util.Iterator<College> iterator = collegeList.iterator();
        while (iterator.hasNext()){
            College next = iterator.next();
            System.out.println("====================="+next.getName()+"===========================");
            print(next.iterator());
        }
    }

    public void print(Iterator iterator){
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}
