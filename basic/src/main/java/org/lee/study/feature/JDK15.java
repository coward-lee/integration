package org.lee.study.feature;

import org.junit.Test;

/**
 * Hidden Classes (JEP 371) ???? 需要了解
 */
public class JDK15 {
    public abstract sealed class Person
            permits Employee, Manager {

        //...
    }

    public final class Employee extends Person {

        public void getEmployeeId() {
            System.out.println("xxxxx Employee");
        }

        public int getYearsOfService() {
            return 0;
        }
    }

    public non-sealed class Manager extends Person {
        public void getSupervisorId() {
            System.out.println("xxxxx Manager");
        }
    }
    // 需要permits  列表中存在
//    public class Human extends Person{
//
//    }

    @Test
    public void test_seal() {
        Person person = new Employee();
        pr(new Manager());
        pr(person);

    }
    public void pr(Person person){

        if (person instanceof Employee) {
            ((Employee) person).getEmployeeId();
        } else if (person instanceof Manager) {
            ((Manager) person).getSupervisorId();
        }
        // jdk 15
        if (person instanceof Employee employee && employee.getYearsOfService() > 5) {
        }
    }
}
