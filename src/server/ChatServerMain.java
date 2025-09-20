package server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import common.interfaces.ChatServer;

public class ChatServerMain {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            ChatServerImpl server = new ChatServerImpl();
            Naming.rebind("rmi://localhost/ChatServer", server);
            System.out.println("Chat Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
