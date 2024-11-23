package org.lee.mvc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileController {


    @RequestMapping("/demo")
    public String file(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        System.out.println(multipartFile.getBytes().length);
        return "";
    }
}
