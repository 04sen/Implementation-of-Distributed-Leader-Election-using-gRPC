/**
 * Copyright (c) [2024] [Sachinandan Das Sen]
 * This file is part of [Implementation of Distributed Leader Election using gRPC], which is released under the MIT License.\
 * See LICENSE.md for details or visit https://opensource.org/licenses/MIT.
 * 
 * 
 * @author Sachinandan Das Sen
 * @date 26 October 2022
 * 
 * LeaderElectionClient is a client class that provides methods for interacting with the LeaderElection service.
 * It allows users to register peers, start elections, and declare coordinators.
 */

package com.example.grpc;

import com.example.grpc.HelloWorldProto.CoordinatorRequest;
import com.example.grpc.HelloWorldProto.CoordinatorResponse;
import com.example.grpc.HelloWorldProto.ElectionRequest;
import com.example.grpc.HelloWorldProto.ElectionResponse;
import com.example.grpc.HelloWorldProto.PeerRequest;
import com.example.grpc.HelloWorldProto.PeerResponse;
import com.example.grpc.LeaderElectionGrpc.LeaderElectionBlockingStub;
import com.example.grpc.PeerRegistrationGrpc.PeerRegistrationBlockingStub;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class LeaderElectionClient {
    private final LeaderElectionBlockingStub electionStub;
    private final PeerRegistrationBlockingStub registrationStub;

    public LeaderElectionClient(ManagedChannel channel) {
        electionStub = LeaderElectionGrpc.newBlockingStub(channel);
        registrationStub = PeerRegistrationGrpc.newBlockingStub(channel);
    }

    /**
     * Registers a peer with the peer registration service. This is a blocking call.
     * 
     * @param nodeId    The ID of the node to register.
     * @param address   The hostname or IP address of the node.
     * @param port      The port number to use for communication with the node.
     */
    public void registerPeer(int nodeId, String address, int port) {
        // Create a PeerRequest object to hold the details of the peer
        PeerRequest request = PeerRequest.newBuilder()
                .setNodeId(nodeId)  // Set the node ID of the peer
                .setAddress(address) // Set the address of the peer
                .setPort(port)       // Set the port of the peer
                .build();

        // Use the stub to send the request to the registration service
        PeerResponse response = registrationStub.registerPeer(request);

        // Print the result of the registration to the console
        System.out.println(response.getStatus());
    }

    /**
     * Starts an election for the given node ID. This is a blocking call.
     * 
     * @param nodeId The ID of the node to start the election for.
     * @return The highest ID found in the election.
     */
    public int startElection(int nodeId) {
        // Create an ElectionRequest object to hold the node ID of the starting node
        ElectionRequest request = ElectionRequest.newBuilder().setNodeId(nodeId).build();

        // Use the stub to send the request to the election service
        ElectionResponse response = electionStub.startElection(request);

        // Print out the result of the election to the console
        System.out.println("Election response: " + response.getStatus() + ", Highest ID: " + response.getMaxId());

        // Return the highest ID found in the election
        return response.getMaxId();
    }

    /**
     * Declare a given node as the leader. This is a blocking call.
     * 
     * @param leaderId The ID of the node to declare as the leader.
     */
    public void declareCoordinator(int leaderId) {
        // Create a CoordinatorRequest message to hold the ID of the leader
        CoordinatorRequest request = CoordinatorRequest.newBuilder().setLeaderId(leaderId).build();
        
        // Use the election stub to send the request to the election service
        CoordinatorResponse response = electionStub.coordinator(request);
        
        // Print out the result of the declaration to the console
        System.out.println("Coordinator declaration response: " + response.getStatus());
    }

    public static void main(String[] args) {
        // Create a channel to communicate with the server
        // This is a blocking channel, meaning that calls to the server
        // will wait until the server responds
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        
        // Create a client that will use this channel to communicate with the server
        LeaderElectionClient client = new LeaderElectionClient(channel);

        // Register a set of peers with the server
        // Each peer is identified by a unique ID, an address, and a port number
        // The address and port number are used to communicate with the peer
        // Resgister as many peers as you like
        System.out.println("Registering peers...");
        client.registerPeer(5, "localhost", 50051);
        client.registerPeer(7, "localhost", 50052);
        client.registerPeer(1, "localhost", 50053);
        client.registerPeer(3, "localhost", 50054);
        client.registerPeer(9, "localhost", 50054);
        client.registerPeer(2, "localhost", 50054);
        client.registerPeer(4, "localhost", 50054);
        client.registerPeer(8, "localhost", 50054);

        // Start elections for each of the peers
        // The startElection() method returns the highest ID found in the election
        // The highest ID is tracked dynamically here
        int maxNodeId = Integer.MIN_VALUE;
        System.out.println("Starting elections...");
        maxNodeId = Math.max(maxNodeId, client.startElection(5));
        maxNodeId = Math.max(maxNodeId, client.startElection(7));
        maxNodeId = Math.max(maxNodeId, client.startElection(1));
        maxNodeId = Math.max(maxNodeId, client.startElection(9));
        maxNodeId = Math.max(maxNodeId, client.startElection(2));
        maxNodeId = Math.max(maxNodeId, client.startElection(4));
        maxNodeId = Math.max(maxNodeId, client.startElection(8));
        maxNodeId = Math.max(maxNodeId, client.startElection(3));

        // Declare the actual leader
        // The declareCoordinator() method takes the ID of the leader node
        // and sends a message to the server to declare it as the leader
        System.out.println("Declaring the leader...");
        client.declareCoordinator(maxNodeId);
        
        // Shutdown the channel to free up server resources
        System.out.println("Shutting down channel...");
        channel.shutdown();
    }
}
