/**
 * Copyright (c) [2024] [Sachinandan Das Sen]
 * This file is part of [Implementation of Distributed Leader Election using gRPC], which is released under the MIT License.\
 * See LICENSE.md for details or visit https://opensource.org/licenses/MIT.
 * 
 * 
 * @author Sachinandan Das Sen
 * @date 26 October 2022
 * 
 * PeerRegistrationServiceImpl is a service implementation class that provides the business logic for the PeerRegistration service.
 * It handles incoming requests from clients and manages the peer registration process.
 */

package com.example.grpc;

import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;

import com.example.grpc.HelloWorldProto.EmptyRequest;
import com.example.grpc.HelloWorldProto.Peer;
import com.example.grpc.HelloWorldProto.PeerList;
import com.example.grpc.HelloWorldProto.PeerRequest;
import com.example.grpc.HelloWorldProto.PeerResponse;


public class PeerRegistrationServiceImpl extends PeerRegistrationGrpc.PeerRegistrationImplBase {
    private final List<Peer> registeredPeers = new ArrayList<>();

    @Override
    public void registerPeer(PeerRequest request, StreamObserver<PeerResponse> responseObserver) {
        // Create a new Peer object from the incoming request details
        Peer peer = Peer.newBuilder()
                .setNodeId(request.getNodeId())  // Set the node ID of the peer
                .setAddress(request.getAddress()) // Set the address of the peer
                .setPort(request.getPort())       // Set the port of the peer
                .build();
        
        // Add the newly created Peer object to the list of registered peers
        registeredPeers.add(peer);
        
        // Log the registration details of the peer
        System.out.println("Peer registered: Node " + peer.getNodeId() + " at " + peer.getAddress() + ":" + peer.getPort());

        // Create a response indicating successful registration of the peer
        PeerResponse response = PeerResponse.newBuilder()
                .setStatus("Node " + peer.getNodeId() + " registered successfully") // Set the status message
                .build();
        
        // Send the response back to the client
        responseObserver.onNext(response);
        
        // Complete the response stream
        responseObserver.onCompleted();
    }

    /**
     * Handles the 'getPeers' RPC request from the client
     * @param request EmptyRequest object (not used in this implementation)
     * @param responseObserver StreamObserver to send the response back to the client
     */
    @Override
    public void getPeers(EmptyRequest request, StreamObserver<PeerList> responseObserver) {
        // Create a Builder for the PeerList message
        PeerList.Builder peerListBuilder = PeerList.newBuilder();
        
        // Add all the registered peers to the PeerList message
        peerListBuilder.addAllPeers(registeredPeers);
        
        // Build the PeerList message from the Builder
        PeerList peerList = peerListBuilder.build();
        
        // Send the PeerList message back to the client as the response
        responseObserver.onNext(peerList);
        
        // Complete the response stream
        responseObserver.onCompleted();
    }
}