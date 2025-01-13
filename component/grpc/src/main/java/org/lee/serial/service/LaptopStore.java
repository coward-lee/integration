package org.lee.serial.service;

import com.gitlab.techschool.pcbook.pb.Filter;
import com.gitlab.techschool.pcbook.pb.Laptop;
import io.grpc.Context;

import java.util.Map;
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

    public void searchLaptop(Context context, Filter filter, LaptopStream laptopStream){
        for (Map.Entry<String, Laptop> stringLaptopEntry : store.entrySet()) {
            if (context.isCancelled()){
                log.info("client is canceled");
                return;
            }
            Laptop laptop = stringLaptopEntry.getValue();
            if (isQulified(filter,laptop )){
                laptopStream.send(laptop.toBuilder().build());
            }
        }
    }

    private boolean isQulified(Filter filter, Laptop laptop) {
        return  true;
    }
}
