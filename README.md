## **CS 3700**

This course covered the fundamentals of networking and distributed computing. Content included packet-switched networks, transport-layer protocols, socket programming, and HTTP communication. The projects in this repository involve hands-on implementation of client-server architectures utilizing a virtualized networking environment.

## **Projects**

This repository contains various Java-based networking projects completed for CS3700, focusing on practical applications of client-server communication and protocol implementation. Below is an overview of the implemented projects:

- **UDP Client-Server Communication (server_HW2.java, client_HW2.java):**  
Implemented a UDP-based client-server system where the client sends item ID requests, and the server responds with product details. The project explores packet-switched networking, request-response communication, and real-time round-trip time (RTT) measurement.

- **Multithreaded HTTP Server & Client (server_HW3.java, client_HW3.java):**  
Developed a multithreaded HTTP server that handles GET requests, serving files over TCP. The client sends HTTP requests, processes responses, and saves retrieved files locally. The project demonstrates socket programming, HTTP request formatting, and response handling.

- **Dijkstra's Shortest Path & Forwarding Table (router.java – HW10):**  
Implemented Dijkstra’s algorithm in Java to simulate a router building its shortest-path tree from a cost topology file. The program reads node-to-node costs from an input file, calculates the shortest path from the source router (V0) to all others, and displays step-by-step updates to the distance and predecessor tables. Finally, it generates a forwarding table indicating which link to use to reach each destination. This project emphasizes link-state routing, cost-based pathfinding, and forwarding logic in network protocols.

Each project demonstrates networking concepts, focusing on protocol implementation, client-server communication, socket programming, and performance evaluation.
