package org.lee.util;

import java.nio.ByteBuffer;

public class ByteBufferUtils {
    public static void print(ByteBuffer buffer) {
//        ByteBuffer buffer = byteBuffer.duplicate();
        System.out.println(buffer);
        int capacity = buffer.capacity();
        StringBuilder stringBuilder = new StringBuilder();
        String position = "pos :\t";
        String bytes  = "byte:\t";
        String word = "word：\t";

        int marginSize = capacity * 4 + 7 + position.length();
        for (int i = 0; i < marginSize; i++) {
            stringBuilder.append("-");
        }
        stringBuilder.append('\n').append("|\t");

        stringBuilder.append(position);
        for (int i = 0; i < capacity; i++) {
            stringBuilder.append(i).append("\t");
        }
        stringBuilder.append('|').append('\n').append("|\t");


        stringBuilder.append(bytes);
        for (int i = 0; i < capacity; i++) {
            if (i < buffer.limit()) {
                stringBuilder.append(buffer.get(i));
            } else {
                stringBuilder.append(0x00);
            }
            stringBuilder.append("\t");
        }
        stringBuilder.append("|").append('\n').append("|\t");


        stringBuilder.append(word);
        buffer.rewind();
        for (int i = 0; i < capacity; i++) {
            if (i < buffer.limit()) {
                stringBuilder.append((char) buffer.get(i));
            } else {
                stringBuilder.append(0x00);
            }
            stringBuilder.append("\t");
        }

        stringBuilder.append('|').append('\n');
        for (int i = 0; i < marginSize; i++) {
            stringBuilder.append("-");
        }
        System.out.println(stringBuilder.toString());
    }
}
