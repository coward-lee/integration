package org.lee.serial.service;

import com.gitlab.techschool.pcbook.pb.CreateLaptopRequest;
import com.gitlab.techschool.pcbook.pb.CreateLaptopResponse;
import com.gitlab.techschool.pcbook.pb.Laptop;
import com.gitlab.techschool.pcbook.pb.LaptopServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.UUID;
import java.util.logging.Logger;

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

}
