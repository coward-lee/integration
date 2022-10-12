package org.lee.gprc;

import com.gitlab.techschool.pcbook.pb.CreateLaptopRequest;
import com.gitlab.techschool.pcbook.pb.CreateLaptopResponse;
import com.gitlab.techschool.pcbook.pb.Laptop;
import com.gitlab.techschool.pcbook.pb.LaptopServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lee.serial.Generator;
import org.lee.serial.service.LaptopServer;
import org.lee.serial.service.LaptopStore;

import java.util.UUID;

public class TestLaptop {
    @Rule
    public final GrpcCleanupRule grpcCleanupRule = new GrpcCleanupRule();

    private LaptopStore laptopStore;
    private LaptopServer laptopServer;
    private ManagedChannel managedChannel;


    @BeforeEach
    public void setup() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        InProcessServerBuilder inProcessServerBuilder = InProcessServerBuilder.forName(serverName).directExecutor();

        laptopStore = new LaptopStore();
        laptopServer = new LaptopServer(inProcessServerBuilder, 0, laptopStore);
        laptopServer.start();

        managedChannel = grpcCleanupRule.register(InProcessChannelBuilder.forName(serverName).directExecutor().build());
    }

    @AfterEach
    public void after() throws Exception {
        laptopServer.stop();
    }

    @Test
    public void createValidLaptop() {
        Laptop laptop = Generator.genNewLaptop();
        CreateLaptopRequest request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();

        LaptopServiceGrpc.LaptopServiceBlockingStub stub = LaptopServiceGrpc.newBlockingStub(managedChannel);
        CreateLaptopResponse response = stub.createLaptop(request);
        Assertions.assertNotNull(response.getId());
        Laptop laptop1 = laptopStore.find(response.getId());
        Assertions.assertNotNull(laptop1);
    }

    @Test
    public void createEmptyIdLaptop() {
        Laptop laptop = Generator.genNewLaptop().toBuilder().setId("").build();
        CreateLaptopRequest request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();

        LaptopServiceGrpc.LaptopServiceBlockingStub stub = LaptopServiceGrpc.newBlockingStub(managedChannel);
        CreateLaptopResponse response = stub.createLaptop(request);
        Assertions.assertNotNull(response.getId());
        Assertions.assertFalse(response.getId().isEmpty());

        Laptop laptop1 = laptopStore.find(response.getId());
        Assertions.assertNotNull(laptop1);
    }

    @Test
    public void createInvalidLaptop() {
        Laptop laptop = Generator.genNewLaptop().toBuilder().setId("invalid").build();
        CreateLaptopRequest request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();

        LaptopServiceGrpc.LaptopServiceBlockingStub stub = LaptopServiceGrpc.newBlockingStub(managedChannel);
        Assertions.assertThrows(StatusRuntimeException.class, () -> stub.createLaptop(request));
    }

    @Test
    public void createExistsLaptop() throws InterruptedException {

        Laptop laptop = Generator.genNewLaptop().toBuilder().setId(UUID.randomUUID().toString()).build();
        CreateLaptopRequest request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();
        laptopStore.save(laptop);
        LaptopServiceGrpc.LaptopServiceBlockingStub stub = LaptopServiceGrpc.newBlockingStub(managedChannel);
        Assertions.assertThrows(StatusRuntimeException.class, () -> {
            stub.createLaptop(request);
        });
        Thread.sleep(1000);

        //        Laptop laptop = Generator.genNewLaptop();
//
//        CreateLaptopRequest request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();
//        LaptopServiceGrpc.LaptopServiceBlockingStub stub = LaptopServiceGrpc.newBlockingStub(managedChannel);
//
//        Assertions.assertThrows(StatusRuntimeException.class, () ->  stub.createLaptop(request));
    }

}
