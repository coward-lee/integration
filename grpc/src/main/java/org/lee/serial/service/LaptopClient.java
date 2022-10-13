package org.lee.serial.service;

import com.gitlab.techschool.pcbook.pb.*;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import org.lee.serial.Generator;

import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class LaptopClient {
    private static final Logger log = Logger.getLogger(LaptopClient.class.getName());
    private final ManagedChannel channel;
    private final LaptopServiceGrpc.LaptopServiceBlockingStub blockingStub;
    private final LaptopServiceGrpc.LaptopServiceStub asyncStub;

    public LaptopClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = LaptopServiceGrpc.newBlockingStub(channel);
        asyncStub = LaptopServiceGrpc.newStub(channel);
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
            } else if (statusException.getStatus().getCode() == Status.Code.INVALID_ARGUMENT) {
                log.info("laptop id:" + laptop.getId() + " INVALID_ARGUMENT");
                return;
            }
        } catch (Exception e) {
            log.info("request fail!" + e.getMessage());
            return;
        }
        log.info("laptop create with ID:" + response.getId());
    }

    private void searchLaptop(Filter filter) {
        log.info("filter");
        SearchLaptopRequest request = SearchLaptopRequest.newBuilder().setFilter(filter).build();
        blockingStub.searchLaptop(request).forEachRemaining(searchLaptopResponse -> {
            log.info("searched result:" + searchLaptopResponse.getLaptop());
        });
        log.info("search completed");

    }


    private void streamClient() throws InterruptedException {
        log.info("filter");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        StreamObserver<RateLaptopRequest> streamObserver = asyncStub
                .rateLaptop(new StreamObserver<>() {
                    @Override
                    public void onNext(RateLaptopResponse value) {
                        log.info("id:" + value.getLaptopId() + " average score" + value.getAverageScore());
                    }

                    @Override
                    public void onError(Throwable t) {
                        log.warning(t.getMessage());
                        countDownLatch.countDown();
                    }

                    @Override
                    public void onCompleted() {
                        countDownLatch.countDown();
                    }
                });
        try {
            for (int i = 0; i < 100; i++) {
                RateLaptopRequest request = RateLaptopRequest.newBuilder()
                        .setLaptopId("laptop id " + i)
                        .setScore(i)
                        .build();
                streamObserver.onNext(request);
                log.info("" + request);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
        boolean terminated = countDownLatch.await(1, TimeUnit.MINUTES);

        if (terminated) {
            log.info("stream terminated");
        } else {
            log.info("stream not terminated");
        }
        streamObserver.onCompleted();

    }


    public static void main(String[] args) {


//        LaptopClient client = new LaptopClient("localhost", 8080);
//        try {
//            for (int i = 0; i < 10; i++) {
//                client.createLaptop(Generator.genNewLaptop());
//            }
//            client.searchLaptop(Filter.getDefaultInstance());
//        } finally {
//            client.shutdown();
//        }


        LaptopClient client = new LaptopClient("localhost", 8080);
        try {
            client.createLaptop(Generator.genNewLaptop());

            client.streamClient();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            client.shutdown();
        }
    }

    private void shutdown() {
        channel.shutdown();
    }
}
