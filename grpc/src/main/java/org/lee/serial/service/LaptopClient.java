package org.lee.serial.service;

import com.gitlab.techschool.pcbook.pb.*;
import io.grpc.*;
import org.lee.serial.Generator;

import java.util.UUID;
import java.util.logging.Logger;

public class LaptopClient {
    private static final Logger log = Logger.getLogger(LaptopClient.class.getName());
    private final ManagedChannel channel;
    private final LaptopServiceGrpc.LaptopServiceBlockingStub blockingStub;

    public LaptopClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = LaptopServiceGrpc.newBlockingStub(channel);
    }

    public void createLaptop(Laptop laptop) {
        CreateLaptopRequest request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();
        CreateLaptopResponse response = CreateLaptopResponse.getDefaultInstance();

        try {
            response = blockingStub.createLaptop(request);
        } catch (StatusRuntimeException statusException) {
            if (statusException.getStatus().getCode() == Status.Code.ALREADY_EXISTS) {
                log.info("laptop id:" + laptop.getId() + " exist");
                return;
            }else if (statusException.getStatus().getCode() == Status.Code.INVALID_ARGUMENT) {
                log.info("laptop id:" + laptop.getId() + " INVALID_ARGUMENT");
                return;
            }
        } catch (Exception e) {
            log.info("request fail!" + e.getMessage());
            return;
        }
        log.info("laptop create with ID:" + response.getId());
    }

    public static void main(String[] args) {


        LaptopClient client = new LaptopClient("localhost", 8080);
        try {
            client.createLaptop(Generator.genNewLaptop());
        } finally {
            client.shutdown();
        }
    }

    private void shutdown() {
        channel.shutdown();
    }
}
