package com.sgc.service;

import com.sgc.protobuf.IdentityVerifyGrpc;
import com.sgc.protobuf.IdentityVerifyOuterClass;
import io.grpc.stub.StreamObserver;

public class IdentityVerifyImpl extends IdentityVerifyGrpc.IdentityVerifyImplBase {
    @Override
    public void addPhoneToUser(IdentityVerifyOuterClass.VerifyRequest request, StreamObserver<IdentityVerifyOuterClass.VerifyResponse> responseObserver) {
        // 认证响应
        IdentityVerifyOuterClass.VerifyResponse response=null;
        // 设置响应对象
        responseObserver.onNext(response);
        // 触发事件
        responseObserver.onCompleted();
    }
}
