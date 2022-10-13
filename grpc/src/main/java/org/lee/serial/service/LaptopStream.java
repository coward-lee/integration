package org.lee.serial.service;

import com.gitlab.techschool.pcbook.pb.Laptop;

public interface LaptopStream {
    void send(Laptop laptop);
}
