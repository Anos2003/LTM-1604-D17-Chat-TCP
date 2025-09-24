package client;

import common.ChatService;

import java.rmi.Naming;

public class ChatClientApp {
    public static void main(String[] args) {
        try {
            ChatService svc = (ChatService) Naming.lookup("rmi://localhost:1099/ChatService");
            System.out.println("Found ChatService: " + svc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
