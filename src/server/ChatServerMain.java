package server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ChatServerMain {
    public static void main(String[] args) {
        try {
            int port = 1099;
            // create registry - if already exists this throws; ensure no other RMI uses it
            try {
                LocateRegistry.createRegistry(port);
                System.out.println("[SERVER] RMI registry created on port " + port);
            } catch (Exception ex) {
                System.out.println("[SERVER] RMI registry may already be running on port " + port);
            }

            ChatServiceImpl impl = new ChatServiceImpl();
            Naming.rebind("rmi://localhost:" + port + "/ChatService", impl);
            System.out.println("[SERVER] ChatService bound to rmi://localhost:" + port + "/ChatService");
            System.out.println("[SERVER] Server ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
