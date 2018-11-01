package com.sgc;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sgc.protobuf.PhoneServiceGrpc;
import com.sgc.protobuf.Phonebook.AddPhoneToUserRequest;
import com.sgc.protobuf.Phonebook.AddPhoneToUserResponse;
import com.sgc.protobuf.Phonebook.PhoneType;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;


public class GRpcClientBlock {

    private static final Logger logger = LoggerFactory.getLogger(GRpcClientBlock.class);

    private final ManagedChannel channel;

    private final PhoneServiceGrpc.PhoneServiceBlockingStub blockingStub;

    /** Construct client connecting to gRPC server at {@code host:port}. */
    public GRpcClientBlock(String host, int port) {
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder.forAddress(host, port).usePlaintext();
        channel = channelBuilder.build();
        blockingStub = PhoneServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /** add phone to user. */
    public void addPhoneToUser(int uid, PhoneType phoneType, String phoneNubmer) {
        logger.info("Will try to add phone to user " + uid);
        AddPhoneToUserRequest request = AddPhoneToUserRequest.newBuilder().setUid(uid).setPhoneType(phoneType)
                .setPhoneNumber(phoneNubmer).build();
        AddPhoneToUserResponse response;
        try {
            response = blockingStub.addPhoneToUser(request);
        } catch (StatusRuntimeException e) {
            logger.warn("RPC failed: {0} --> "+e.getLocalizedMessage(), e.getStatus());
            return;
        }
        logger.info("Result: " + response.getResult());
    }

    public static void main(String[] args) throws Exception {
        GRpcClientBlock client = new GRpcClientBlock("localhost", 18888);
        try {
            client.addPhoneToUser(1, PhoneType.WORK, "15609077835");
        } finally {
            client.shutdown();
        }
    }

}