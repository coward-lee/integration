package org.lee.serial.service;

import com.lee.bistream.BiStreamRequest;
import com.lee.bistream.BiStreamResponse;
import com.lee.bistream.BiStreamServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.atomic.AtomicInteger;

public class BiStreamService extends BiStreamServiceGrpc.BiStreamServiceImplBase {
    @Override
    public StreamObserver<BiStreamRequest> biStreamDemo(StreamObserver<BiStreamResponse> responseObserver) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        return new StreamObserver<>() {
            @Override
            public void onNext(BiStreamRequest value) {
                System.out.println(value);
                // 客户端的 推送人员
                int i = atomicInteger.incrementAndGet();
                BiStreamResponse response = BiStreamResponse.newBuilder()
                        .setContent("服务端" + i)
                        .setEntityId(value.getEntityId())
                        .setIndex(i)
                        .build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                System.out.println("客户端的 on completed");
                responseObserver.onCompleted();
            }
        };
    }
}
