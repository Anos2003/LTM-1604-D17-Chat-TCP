package common.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import common.model.Message;
import common.model.User;

public interface ChatServer extends Remote {
    void registerUser(User user) throws RemoteException;
    void sendMessage(Message message, String groupName) throws RemoteException;
    List<Message> getMessages(String groupName) throws RemoteException;
}
