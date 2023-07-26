package org.lee.flight.server;

import org.apache.arrow.flight.*;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.VectorLoader;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.VectorUnloader;
import org.apache.arrow.vector.ipc.message.ArrowRecordBatch;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FlightServerStepTest {
    BufferAllocator allocator = new RootAllocator();

    @Test
    void test_server() {
        Location location = Location.forGrpcInsecure("0.0.0.0", 33333);
        try (
                final FlightServerTest.CookbookProducer producer = new FlightServerTest.CookbookProducer(allocator, location);
                final FlightServer flightServer = FlightServer.builder(allocator, location, producer).build()
        ) {
            flightServer.start();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    public static class MyProducer extends NoOpFlightProducer{
        private final BufferAllocator allocator;
        private final Location location;
        private final ConcurrentMap<FlightDescriptor, FlightServerTest.Dataset> datasets;

        public MyProducer(BufferAllocator allocator, Location location) {
            this.allocator = allocator;
            this.location = location;
            this.datasets = new ConcurrentHashMap<>();
        }
        @Override
        public void getStream(CallContext context, Ticket ticket, ServerStreamListener listener) {
            FlightDescriptor flightDescriptor = FlightDescriptor.path(
                    new String(ticket.getBytes(), StandardCharsets.UTF_8));
            FlightServerTest.Dataset dataset = this.datasets.get(flightDescriptor);
            if (dataset == null) {
                throw CallStatus.NOT_FOUND.withDescription("Unknown descriptor").toRuntimeException();
            }
            try (VectorSchemaRoot root = VectorSchemaRoot.create(
                    this.datasets.get(flightDescriptor).getSchema(), allocator)) {
                VectorLoader loader = new VectorLoader(root);
                listener.start(root);
                for (ArrowRecordBatch arrowRecordBatch : this.datasets.get(flightDescriptor).getBatches()) {
                    loader.load(arrowRecordBatch);
                    listener.putNext();
                }
                listener.completed();
            }
        }

        @Override
        public void listFlights(CallContext context, Criteria criteria, StreamListener<FlightInfo> listener) {
            super.listFlights(context, criteria, listener);
        }

        @Override
        public FlightInfo getFlightInfo(CallContext context, FlightDescriptor descriptor) {
            return super.getFlightInfo(context, descriptor);
        }

        @Override
        public Runnable acceptPut(CallContext context, FlightStream flightStream, StreamListener<PutResult> ackStream) {
            List<ArrowRecordBatch> batches = new ArrayList<>();
            return () -> {
                long rows = 0;
                VectorUnloader unloader;
                while (flightStream.next()) {
                    unloader = new VectorUnloader(flightStream.getRoot());
                    final ArrowRecordBatch arb = unloader.getRecordBatch();
                    batches.add(arb);
                    rows += flightStream.getRoot().getRowCount();
                }
                FlightServerTest.Dataset dataset = new FlightServerTest.Dataset(batches, flightStream.getSchema(), rows);
                datasets.put(flightStream.getDescriptor(), dataset);
                ackStream.onCompleted();
            };
        }

        @Override
        public void doAction(CallContext context, Action action, StreamListener<Result> listener) {
            super.doAction(context, action, listener);
        }

        @Override
        public void listActions(CallContext context, StreamListener<ActionType> listener) {
            super.listActions(context, listener);
        }
    }
}