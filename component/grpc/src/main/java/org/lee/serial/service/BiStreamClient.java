package org.lee.serial.service;

import com.gitlab.techschool.pcbook.pb.LaptopServiceGrpc;
import com.lee.bistream.BiStreamRequest;
import com.lee.bistream.BiStreamResponse;
import com.lee.bistream.BiStreamServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class BiStreamClient {
    private static final Logger log = Logger.getLogger(BiStreamClient.class.getName());
    private final BiStreamServiceGrpc.BiStreamServiceStub asyncStub;

    public BiStreamClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        asyncStub = BiStreamServiceGrpc.newStub(channel);
    }

    public void send() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        StreamObserver<BiStreamRequest> streamObserver = asyncStub
                .biStreamDemo(new StreamObserver<>() {
                    @Override
                    public void onNext(BiStreamResponse value) {
                        System.out.println("从服务端返回的：" + value);

                    }
                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        countDownLatch.countDown();
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("服务端的 on completed");
                        countDownLatch.countDown();
                    }
                });
        for (int i = 0; i < 100; i++) {
            BiStreamRequest biStreamEntity = BiStreamRequest.newBuilder().setEntityId("id" + i).setContent("content" + i).build();
            Thread.sleep(10);
            streamObserver.onNext(biStreamEntity);
            System.out.println(biStreamEntity);
        }
        streamObserver.onCompleted();

        if (countDownLatch.await(1, TimeUnit.MINUTES)) {
            System.out.println("传送完成");
        } else {
            System.out.println("传送完成失败");
        }

    }

    public static void main(String[] args) throws InterruptedException {
        new BiStreamClient("localhost", 8080).send();
    }
}
