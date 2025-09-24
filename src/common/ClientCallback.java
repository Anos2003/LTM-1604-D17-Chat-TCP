package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallback extends Remote {
    void onMessage(Message m) throws RemoteException;
    void onInfo(String info) throws RemoteException;
}
