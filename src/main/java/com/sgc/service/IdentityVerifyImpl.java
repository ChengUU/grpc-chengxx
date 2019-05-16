package com.sgc.service;

import com.sgc.protobuf.IdentityVerifyGrpc;
import com.sgc.protobuf.IdentityVerifyOuterClass;
import io.grpc.stub.StreamObserver;

import javax.xml.ws.soap.AddressingFeature;

public class IdentityVerifyImpl extends IdentityVerifyGrpc.IdentityVerifyImplBase {
    @Override
    public void verify(IdentityVerifyOuterClass.VerifyRequest request, StreamObserver<IdentityVerifyOuterClass.VerifyResponse> responseObserver) {
        // 认证响应
        IdentityVerifyOuterClass.VerifyResponse response=null;
        response=IdentityVerifyOuterClass.VerifyResponse.newBuilder()
                .setState(true)
                .setMessage("Test-Verify Successful")
                .setName("Test-User")
                .setPassword("Test-Password")
                .build();
        // 设置响应对象
        responseObserver.onNext(response);
        // 触发事件
        responseObserver.onCompleted();
    }
}
