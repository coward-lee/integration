package org.lee.serial.service;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.inprocess.InProcessServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class LaptopServer {
    private final Logger log = Logger.getLogger(LaptopServer.class.getName());

    private final int port;
    private final Server server;

    public LaptopServer(int port, LaptopStore laptopStore) {
        this(ServerBuilder.forPort(port), port, laptopStore);
    }

    public LaptopServer(ServerBuilder serverBuilder, int port, LaptopStore laptopStore) {
        this.port = port;
        LapTopService lapTopService = new LapTopService(laptopStore);
        server = serverBuilder.addService(lapTopService).build();
    }

    public LaptopServer(InProcessServerBuilder inProcessServerBuilder, int port, LaptopStore laptopStore) {

        this.port = port;

        LapTopService lapTopService = new LapTopService(laptopStore);
        server = inProcessServerBuilder.addService(lapTopService).build();
    }

    public void start() throws IOException {
        server.start();
        log.info("server started on port " + port);


        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("shut down grpc with jvm down");
                try {
                    LaptopServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
            }
        });
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public void blockStop() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws Exception {
        LaptopServer laptopServer = new LaptopServer(8080,new LaptopStore());
        laptopServer.start();
        laptopServer.blockStop();
    }


}
