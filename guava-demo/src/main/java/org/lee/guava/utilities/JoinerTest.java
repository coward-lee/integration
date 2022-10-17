package org.lee.guava.utilities;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;

public class JoinerTest {

    String dir = "./tmp/";
    @Test
    void test_start(){
        System.out.println("xxx");
    }

    @Test
    void test_join(){
        String join = Joiner.on("#").join(ImmutableList.of("1", "2", "3"));
        System.out.println(join);
    }

    @Test
    void test_append_to_writer()  {
        File file = new File(dir + "guava_to_writer.txt");
        try(FileWriter fileWriter = new FileWriter(file)){
            if (!file.exists()) file.createNewFile();
            Joiner.on("#").appendTo(fileWriter, ImmutableList.of("1", "2", "3"));
            Joiner.on("#").appendTo(fileWriter, ImmutableList.of("1", "2", "3"));
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
