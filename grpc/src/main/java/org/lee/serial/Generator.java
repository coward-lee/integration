package org.lee.serial;

import com.gitlab.techschool.pcbook.pb.CPU;
import com.gitlab.techschool.pcbook.pb.Keyboard;
import com.gitlab.techschool.pcbook.pb.Laptop;

import java.util.UUID;

public class Generator {

    public static Laptop genNewLaptop(){
        return Laptop.newBuilder()
                .setBrand("apple")
                .setId(UUID.randomUUID().toString())
                .setCpu(CPU.newBuilder().setBrand("xxx").build())
                .setKeyboard(Keyboard.newBuilder().setLayout(Keyboard.Layout.AZERTY).build())
                .build();
    }
}
