package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatService extends Remote {
    // Authentication
    boolean register(String username, String password) throws RemoteException;
    boolean login(String username, String password) throws RemoteException;

    // Callback registration so server can push messages/infos
    void registerCallback(String username, ClientCallback cb) throws RemoteException;

    // Group management
    List<String> getGroups() throws RemoteException;
    boolean createGroup(String groupName) throws RemoteException;
    boolean joinGroup(String username, String groupName, String igname) throws RemoteException;

    // Messaging
    void sendMessage(Message m) throws RemoteException;
    void sendFile(Message m) throws RemoteException;
}
