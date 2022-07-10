package org.lee.study;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Main {

    public  void getResource() throws IOException {
        //查找指定资源的URL，其中res.txt仍然开始的bin目录下
        URL fileURL=this.getClass().getResource("/resource/res.txt");
        System.out.println(fileURL.getFile());
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/resource/res.txt");
        byte[] bytes = new byte[1024];
        int read = resourceAsStream.read(bytes, 0, bytes.length);
        System.out.println(new String(bytes, 0, read));
//        System.out.println(resourceAsStream);
    }
    public static void main(String[] args) throws IOException {
        Main res=new Main();
        res.getResource();
    }

}
