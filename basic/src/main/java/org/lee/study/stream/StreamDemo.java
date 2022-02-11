package org.lee.study.stream;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamDemo {

    public static void main(String[] args) {
        new StreamDemo().test_streamSourceAndDest();
    }

    /**
     * 测试 stream前后的引用的改变
     * stream的整个过程不会改变源头的item，如果收集的结果是也是用的原来的元素那么整个是和原来的元素相同的对象，
     * 但是引用对象里面的属性可能会被该变，源头的引用对象里面的属性也会跟着变化
     */
    @Test
    public void test_streamSourceAndDest(){
        List<Person> people = Arrays.asList(new Person(1),new Person(2),new Person(3));
        List<Person> collect = people.stream().filter(person -> {
            person.id += 10;
            return true;
        }).collect(Collectors.toList());
        for (int i = 0; i < people.size(); i++) {
            System.out.println(people.get(i) == collect.get(i));
            System.out.println(people.get(i).id == collect.get(i).id); // true
        }
    }
    private static class Person{
        Integer id;

        public Person(Integer id) {
            this.id = id;
        }
    }

}
