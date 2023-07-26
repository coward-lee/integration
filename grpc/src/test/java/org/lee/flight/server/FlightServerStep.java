//package org.lee.flight.server;
//
//import org.apache.arrow.flight.*;
//import org.apache.arrow.memory.RootAllocator;
//import org.apache.arrow.vector.VarCharVector;
//import org.apache.arrow.vector.VectorSchemaRoot;
//import org.apache.arrow.vector.VectorUnloader;
//import org.apache.arrow.vector.ipc.message.ArrowRecordBatch;
//import org.apache.arrow.vector.types.pojo.Field;
//import org.apache.arrow.vector.types.pojo.FieldType;
//import org.apache.arrow.vector.types.pojo.Schema;
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class FlightServerStep {
//
//    Location location = Location.forGrpcInsecure("0.0.0.0", 33333);
//    RootAllocator allocator = new RootAllocator();
//
//    @Test
//    public void test_server_start() throws InterruptedException {
//        try (FlightServer flightServer = FlightServer.builder(allocator, location,
//                new FlightServerTest.CookbookProducer(allocator, location)).build()) {
//            try {
//                flightServer.start();
//                System.out.println("S1: Server (Location): Listening on port " + flightServer.getPort());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        Thread.sleep(100000);
//    }
//
//    @Test
//    public void test_client_start() throws InterruptedException {
//        try (FlightClient flightClient = FlightClient.builder(allocator, location).build()) {
//            System.out.println("C1: Client (Location): Connected to " + location.getUri());
//        }
//    }
//
//    public Runnable acceptPut(FlightProducer.CallContext context, FlightStream flightStream, FlightProducer.StreamListener<PutResult> ackStream) {
//        List<ArrowRecordBatch> batches = new ArrayList<>();
//        return () -> {
//            long rows = 0;
//            VectorUnloader unloader;
//            while (flightStream.next()) {
//                unloader = new VectorUnloader(flightStream.getRoot());
//                try (final ArrowRecordBatch arb = unloader.getRecordBatch()) {
//                    batches.add(arb);
//                    rows += flightStream.getRoot().getRowCount();
//                }
//            }
//            FlightServerTest.Dataset dataset = new FlightServerTest.Dataset(batches, flightStream.getSchema(), rows);
//            datasets.put(flightStream.getDescriptor(), dataset);
//            ackStream.onCompleted();
//        };
//    }
//
//    // Client
//    Schema schema = new Schema(Arrays.asList(
//            new Field("name", FieldType.nullable(new ArrowType.Utf8()), null)));
//try(
//    VectorSchemaRoot vectorSchemaRoot = VectorSchemaRoot.create(schema, allocator);
//    VarCharVector varCharVector = (VarCharVector) vectorSchemaRoot.getVector("name"))
//
//    {
//        varCharVector.allocateNew(3);
//        varCharVector.set(0, "Ronald".getBytes());
//        varCharVector.set(1, "David".getBytes());
//        varCharVector.set(2, "Francisco".getBytes());
//        vectorSchemaRoot.setRowCount(3);
//        FlightClient.ClientStreamListener listener = flightClient.startPut(
//                FlightDescriptor.path("profiles"),
//                vectorSchemaRoot, new AsyncPutListener());
//        listener.putNext();
//        varCharVector.set(0, "Manuel".getBytes());
//        varCharVector.set(1, "Felipe".getBytes());
//        varCharVector.set(2, "JJ".getBytes());
//        vectorSchemaRoot.setRowCount(3);
//        listener.putNext();
//        listener.completed();
//        listener.getResult();
//        System.out.println("C2: Client (Populate Data): Wrote 2 batches with 3 rows each");
//    }
//}
//}