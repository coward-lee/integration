package org.lee;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource("bean:name=EndPoint")
public class EndPoint {
    @ManagedOperation
    public void demo(){
        System.out.println("成功了吗！");
    }
}
