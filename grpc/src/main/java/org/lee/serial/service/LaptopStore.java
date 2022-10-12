package org.lee.serial.service;

import com.gitlab.techschool.pcbook.pb.Laptop;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class LaptopStore {
    private final Logger log = Logger.getLogger(LaptopStore.class.getName());
    private final ConcurrentHashMap<String, Laptop> store = new ConcurrentHashMap<>();

    public void save(Laptop laptop) {
        if (store.containsKey(laptop.getId())) {
            throw new AlreadyExistsException(laptop.getId() + " is exists");
        }
        log.info("save laptop:" + laptop);
        store.put(laptop.getId(), laptop);
    }

    public Laptop find(String id){
        return store.get(id);
    }
}
