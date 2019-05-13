package com.sgc;

import com.sgc.service.IdentityVerifyImpl;
import com.sgc.service.PhoneServiceImp;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FingerVerifyServer {
    // 日志记录
    private static final Logger logger = LoggerFactory.getLogger(GRpcServerDefault.class);

    /**
     * 服务器对象
     */
    private Server server;

    /**
     * 业务内容
     * @throws IOException
     */
    private void start() throws IOException {
        /* The port on which the server should run */
        int port = 288888;
        /** 构造RPC服务器 */
        server = ServerBuilder
                .forPort(port)
                .addService(new IdentityVerifyImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + port);
        /** 设置服务器关闭钩子(善后处理) */
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("*** shutting down gRPC server since JVM is shutting down");
                FingerVerifyServer.this.stop();
                logger.info("*** server shut down");
            }
        });
    }

    /**
     * 关闭服务
     */
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
}
