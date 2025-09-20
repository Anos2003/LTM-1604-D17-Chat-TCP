package server;
import common.interfaces.ChatServer;

import common.interfaces.ChatClientInterface;
import common.interfaces.ChatServerInterface;
import common.model.Message;
import common.model.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServerInterface {
    private Map<String, List<ChatClientInterface>> groups;
    private Map<String, User> users;

    public ChatServerImpl() throws RemoteException {
        super();
        groups = new HashMap<>();
        users = new HashMap<>();

        // 3 nhóm chat mặc định
        groups.put("Group 1", new ArrayList<>());
        groups.put("Group 2", new ArrayList<>());
        groups.put("Group 3", new ArrayList<>());
    }

    @Override
    public boolean register(User user) throws RemoteException {
        if(users.containsKey(user.getUsername())) return false;
        users.put(user.getUsername(), user);
        return true;
    }

    @Override
    public boolean login(User user, ChatClientInterface client) throws RemoteException {
        return users.containsKey(user.getUsername()) &&
               users.get(user.getUsername()).getPassword().equals(user.getPassword());
    }

    @Override
    public void joinGroup(String groupName, User user, ChatClientInterface client) throws RemoteException {
        if(groups.containsKey(groupName)) {
            groups.get(groupName).add(client);
            for(ChatClientInterface c : groups.get(groupName)) {
                if(!c.equals(client)) {
                    c.notifyUserJoined(groupName, user.getUsername());
                }
            }
        }
    }

    @Override
    public void sendMessage(String groupName, Message message) throws RemoteException {
        if(groups.containsKey(groupName)) {
            for(ChatClientInterface c : groups.get(groupName)) {
                c.receiveMessage(message);
            }
        }
    }

    @Override
    public List<String> getAvailableGroups() throws RemoteException {
        return new ArrayList<>(groups.keySet());
    }
}
