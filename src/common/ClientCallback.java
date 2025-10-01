package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientCallback extends Remote {
    void onMessage(Message m) throws RemoteException;
    void onInfo(String info) throws RemoteException;
	void onUpdateOnlineUsers(List<String> users) throws RemoteException;
}
