/**
 * * Copyright (c) [2024] [Sachinandan Das Sen]
 * This file is part of [Implementation of Distributed Leader Election using gRPC], which is released under the MIT License.\
 * See LICENSE.md for details or visit https://opensource.org/licenses/MIT.
 * 
 * 
 * @author Sachinandan Das Sen
 * @date 26 October 2022
 * 
 * LeaderElectionServer is a server class that implements the LeaderElection service.
 * It handles incoming requests from clients and manages the leader election process.
 */

package com.example.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class LeaderElectionServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        // The port number that our server will listen on
        int port = 50051;

        // Create a new server builder that will create a server that
        // listens on the specified port
        Server server = ServerBuilder.forPort(port)

                // Add the LeaderElectionServiceImpl service to
                // the server
                .addService(new LeaderElectionServiceImpl())

                // Add the PeerRegistrationServiceImpl service to
                // the server
                .addService(new PeerRegistrationServiceImpl())

                // Build the server
                .build()

                // Start the server
                .start();

        // Print a message to let the user know that the server is running
        System.out.println("Server started at port " + port);

        // Wait for the server to terminate
        // This is a blocking call, and the program will not continue
        // until the server is stopped
        server.awaitTermination();
    }
}
