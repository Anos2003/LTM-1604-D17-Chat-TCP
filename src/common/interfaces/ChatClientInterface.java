package common.interfaces;

import common.model.Message;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClientInterface extends Remote {
    void receiveMessage(Message message) throws RemoteException;
    void notifyUserJoined(String groupName, String username) throws RemoteException;
}
