package org.lee.study.rmi;

import javax.management.remote.rmi.RMIServerImpl_Stub;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HelloRMIClient {
    public static void main(String[] argv) throws Exception {
//        String addr = argv[0];
//        int port = Integer.parseInt(argv[1]);
//        String name = argv[2];
//        String sth = argv[3];
        String addr = "192.168.0.182";
        Integer port = 55555;
        Registry r = LocateRegistry.getRegistry(addr, port);

        RMIServerImpl_Stub hello = (RMIServerImpl_Stub)r.lookup("jmxrmi");
        System.out.println(hello);

    }
}