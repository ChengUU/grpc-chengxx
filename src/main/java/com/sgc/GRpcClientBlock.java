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

    /** grpc channel */
    private final ManagedChannel channel;

    /** rpc stub */
    private final PhoneServiceGrpc.PhoneServiceBlockingStub blockingStub;

    /** Construct client connecting to gRPC server at {@code host:port}. */
    public GRpcClientBlock(String host, int port) {
        /** 设置RPC通道相关参数 */
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder.forAddress(host, port).usePlaintext();
        /** 构建RPC通道 */
        channel = channelBuilder.build();
        /** 获取RPC Stub */
        blockingStub = PhoneServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /** add phone to user. */
    public void addPhoneToUser(int uid, PhoneType phoneType, String phoneNubmer) {
        logger.info("Will try to add phone to user " + uid);
        /** 构建请求 */
        AddPhoneToUserRequest request = AddPhoneToUserRequest.newBuilder().setUid(uid).setPhoneType(phoneType)
                .setPhoneNumber(phoneNubmer).build();
        AddPhoneToUserResponse response;
        try {
            /** 通过RPC Stub调用远程方法 */
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