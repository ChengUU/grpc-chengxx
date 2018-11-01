package com.sgc;
import java.io.IOException;

import com.sgc.service.PhoneServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GRpcServerDefault {

    private static final Logger logger = LoggerFactory.getLogger(GRpcServerDefault.class);

    private Server server;

    private void start() throws IOException {
        /* The port on which the server should run */
        int port = 18888;
        server = ServerBuilder
                .forPort(port)
                .addService(new PhoneServiceImp())
                .build()
                .start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                GRpcServerDefault.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon
     * threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Main launches the server from the command line.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        final GRpcServerDefault server = new GRpcServerDefault();
        server.start();
        server.blockUntilShutdown();
    }

}