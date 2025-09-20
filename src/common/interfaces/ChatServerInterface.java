package common.interfaces;

import common.model.Message;
import common.model.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatServerInterface extends Remote {
    boolean register(User user) throws RemoteException;
    boolean login(User user, ChatClientInterface client) throws RemoteException;
    void joinGroup(String groupName, User user, ChatClientInterface client) throws RemoteException;
    void sendMessage(String groupName, Message message) throws RemoteException;
    List<String> getAvailableGroups() throws RemoteException;
}
