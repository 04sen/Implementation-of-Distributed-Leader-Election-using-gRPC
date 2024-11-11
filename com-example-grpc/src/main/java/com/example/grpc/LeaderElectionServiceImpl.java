/**
 * Copyright (c) [2024] [Sachinandan Das Sen]
 * This file is part of [Implementation of Distributed Leader Election using gRPC], which is released under the MIT License.\
 * See LICENSE.md for details or visit https://opensource.org/licenses/MIT.
 * 
 * @author Sachinandan Das Sen
 * @date 26 October 2022
 * 
 * LeaderElectionServiceImpl is a service implementation class that provides the business logic for the LeaderElection service.
 * It handles incoming requests from clients and manages the leader election process.
 */

package com.example.grpc;

import com.example.grpc.HelloWorldProto.CoordinatorRequest;
import com.example.grpc.HelloWorldProto.CoordinatorResponse;
import com.example.grpc.HelloWorldProto.ElectionRequest;
import com.example.grpc.HelloWorldProto.ElectionResponse;

import io.grpc.stub.StreamObserver;

public class LeaderElectionServiceImpl extends LeaderElectionGrpc.LeaderElectionImplBase {
    private int highestId = -1;
    private int leaderId = -1;

    /**
     * This function is invoked by each node when it starts an election.
     * It takes the nodeId of the starting node and compares it with the highestId
     * currently stored in the server. If the nodeId is greater, it updates the
     * highestId.
     * It then sends back the current max ID (i.e. the highestId) to the
     * starting node.
     *
     * @param request the ElectionRequest object containing the nodeId of the
     *                starting node
     * @param responseObserver the object to which we send the ElectionResponse
     */
    @Override
    public void startElection(ElectionRequest request, StreamObserver<ElectionResponse> responseObserver) {
        int nodeId = request.getNodeId();
        System.out.println("Election initiated by Node: " + nodeId);

        // If the nodeId received is greater than the highestId currently stored,
        // update the highestId
        if (nodeId > highestId) {
            highestId = nodeId;
        }

        // Send back the current max ID (i.e. the highestId) to the starting node
        ElectionResponse response = ElectionResponse.newBuilder()
                .setMaxId(highestId)
                .setStatus("Election in progress")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    /**
     * This function is invoked by the leader after an election is completed.
     * It takes the nodeId of the leader and stores it in the leaderId variable.
     * It then sends back a confirmation message to the leader.
     *
     * @param request the CoordinatorRequest object containing the nodeId of the
     *                leader
     * @param responseObserver the object to which we send the CoordinatorResponse
     */
    @Override
    public void coordinator(CoordinatorRequest request, StreamObserver<CoordinatorResponse> responseObserver) {
        // The leaderId is set to the nodeId of the leader
        leaderId = request.getLeaderId();
        System.out.println("New leader is Node " + leaderId);

        // A confirmation message is sent back to the leader
        CoordinatorResponse response = CoordinatorResponse.newBuilder()
                .setStatus("Leader confirmed as Node " + leaderId)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
