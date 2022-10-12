package org.lee.serial;

import com.gitlab.techschool.pcbook.pb.CPU;
import com.gitlab.techschool.pcbook.pb.Keyboard;
import com.gitlab.techschool.pcbook.pb.Laptop;
import com.google.protobuf.util.JsonFormat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class Serializer {

    public void writeBinFile(Laptop laptop, String fileName) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        laptop.writeTo(fileOutputStream);
        fileOutputStream.close();
    }

    public Laptop readBinFile(String fileName) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(fileName);
        Laptop laptop = Laptop.parseFrom(fileInputStream);
        fileInputStream.close();
        return laptop;
    }

    public void writeJSONFile(Laptop laptop, String fileName) throws IOException {
        JsonFormat.Printer printer = JsonFormat.printer().includingDefaultValueFields()
                .preservingProtoFieldNames();
        String json = printer.print(laptop);

        FileOutputStream outputStream = new FileOutputStream(fileName);
        outputStream.write(json.getBytes());
        outputStream.close();
    }


    public static void main(String[] args) throws IOException {
        Serializer serializer = new Serializer();
        Laptop laptopObj = Laptop.newBuilder()
                .setBrand("apple")
                .setCpu(CPU.newBuilder().setBrand("xxx").build())
                .setKeyboard(Keyboard.newBuilder().setLayout(Keyboard.Layout.AZERTY).build())
                .build();
//        serializer.writeBinFile(laptopObj,"laptop.bin");
//        Laptop laptop = serializer.readBinFile("laptop.bin");
//        serializer.writeJSONFile(laptop, "laptop.json");
        boolean equals = Objects.equals(serializer.readBinFile("laptop.bin"), laptopObj);
        System.out.println(equals);
    }
}
