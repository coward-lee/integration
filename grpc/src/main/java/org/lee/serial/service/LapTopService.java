package org.lee.serial.service;

import com.gitlab.techschool.pcbook.pb.*;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.UUID;
import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class LapTopService extends LaptopServiceGrpc.LaptopServiceImplBase {
    private final Logger log = Logger.getLogger(LapTopService.class.getName());
    private final LaptopStore laptopStore;

    public LapTopService(LaptopStore laptopStore) {
        this.laptopStore = laptopStore;
    }

    @Override
    public void createLaptop(CreateLaptopRequest request, StreamObserver<CreateLaptopResponse> responseObserver) {
        Laptop laptop = request.getLaptop();
        String id = laptop.getId();
        log.info("get a create-laptop request with id:" + id);
        UUID uuid = null;
        if (id.isEmpty()) {
            uuid = UUID.randomUUID();
        } else {
            try {
                uuid = UUID.fromString(id);
            } catch (IllegalArgumentException e) {
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription(e.getMessage())
                                .asRuntimeException()
                );
                return;
            }
        }
        Laptop other = laptop.toBuilder().setId(uuid.toString()).build();
        try {
            laptopStore.save(other);
        } catch (AlreadyExistsException e) {
            responseObserver.onError(
                    Status.ALREADY_EXISTS
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        }
        CreateLaptopResponse response = CreateLaptopResponse.newBuilder().setId(other.getId()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        log.info("save success" + response.getId());
    }

    @Override
    public void searchLaptop(SearchLaptopRequest request, StreamObserver<SearchLaptopResponse> responseObserver) {
        Filter filter = request.getFilter();

        laptopStore.searchLaptop(Context.current(), filter, laptop -> responseObserver.onNext(SearchLaptopResponse.newBuilder().setLaptop(laptop).build()));
    }


    @Override
    public StreamObserver<RateLaptopRequest> rateLaptop(StreamObserver<RateLaptopResponse> responseObserver) {
        return new StreamObserver<>() {
            long startTime = System.nanoTime();

            @Override
            public void onNext(RateLaptopRequest value) {
                print(value);
            }

            @Override
            public void onError(Throwable t) {
                log.warning(t.getMessage());
            }

            @Override
            public void onCompleted() {
                long seconds = NANOSECONDS.toSeconds(System.nanoTime() - startTime);
                log.info("completed in " + seconds + "s");
                responseObserver.onCompleted();
            }


        };
    }

    public void print(RateLaptopRequest laptop) {
        log.info("rate laptop:" + laptop.getLaptopId() + " score " + laptop.getScore());
    }
}
