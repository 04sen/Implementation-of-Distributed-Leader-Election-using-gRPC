# Distributed Leader Election System

This is for Assignment 2 of CS324 offered by the University of the South Pacific. Dated: 26 October 2024

A Java-based distributed leader election implementation using gRPC for peer-to-peer communication. This system allows multiple nodes to participate in a leader election process, where the node with the highest ID becomes the leader.

## Overview

This project implements the Bully Algorithm for leader election in a distributed system. It consists of:
- A gRPC server that manages peer registration and election processes
- A client that can register peers and initiate elections
- Protocol buffer definitions for service interfaces
- Service implementations for handling peer registration and election logic

## Features

- Peer registration with custom node IDs, addresses, and ports
- Dynamic leader election process
- Coordinator declaration after election completion
- List management of registered peers
- Fault-tolerant design for distributed systems

## Architecture

The system is built using the following components:

1. **Protocol Buffers (`leader_election.proto`)**
   - Defines the service interfaces and message types
   - Two main services: `LeaderElection` and `PeerRegistration`

2. **Server Components**
   - `LeaderElectionServer`: Main server implementation
   - `LeaderElectionServiceImpl`: Handles election logic
   - `PeerRegistrationServiceImpl`: Manages peer registration

3. **Client Component**
   - `LeaderElectionClient`: Provides methods for interacting with the server

## Prerequisites

- Java 8 or higher
- gRPC dependencies
- Protocol Buffers compiler
- Maven or Gradle (for dependency management)

## Installation

1. Clone the repository
2. Compile the protocol buffers:
   ```bash
   protoc --java_out=src/main/java leader_election.proto
   ```
3. Build the project using Maven/Gradle
4. Run the server and client applications

## Usage

### Starting the Server

```java
run the LeaderElectionServer.java file
```

### Using the Client

```java
run the LeaderElectionClient.java file
```

## Leader Election Process

1. **Peer Registration**
   - Each node registers with a unique ID, address, and port
   - Registration details are stored in the server

2. **Election Process**
   - Any node can initiate an election
   - The system tracks the highest node ID
   - Election messages are propagated through the network

3. **Leader Declaration**
   - The node with the highest ID becomes the leader
   - All nodes are informed of the new leader

## Implementation Details

### Leader Election Service

The `LeaderElectionServiceImpl` class manages the election process:
- Tracks the highest node ID seen during elections
- Maintains the current leader ID
- Handles election requests and coordinator declarations

### Peer Registration Service

The `PeerRegistrationServiceImpl` class handles:
- Registration of new peers
- Maintenance of peer list
- Retrieval of registered peers

## API Reference

### gRPC Services

#### LeaderElection Service
- `startElection(ElectionRequest) returns (ElectionResponse)`
- `coordinator(CoordinatorRequest) returns (CoordinatorResponse)`

#### PeerRegistration Service
- `registerPeer(PeerRequest) returns (PeerResponse)`
- `getPeers(EmptyRequest) returns (PeerList)`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.

## Author

Sachinandan Das Sen

## Version History

- Initial Release: October 26, 2022

## Acknowledgments

- gRPC framework
- Protocol Buffers
- Distributed Systems concepts