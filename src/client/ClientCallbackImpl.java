package client;

import common.ClientCallback;
import common.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.function.Consumer;

public class ClientCallbackImpl extends UnicastRemoteObject implements ClientCallback {
    private static final long serialVersionUID = 1L;

    private final Consumer<Message> onMessage;
    private final java.util.function.Consumer<String> onInfo;

    public ClientCallbackImpl(Consumer<Message> onMessage, java.util.function.Consumer<String> onInfo) throws RemoteException {
        super();
        this.onMessage = onMessage;
        this.onInfo = onInfo;
    }

    @Override
    public void onMessage(Message m) throws RemoteException {
        if (onMessage != null) onMessage.accept(m);
    }

    @Override
    public void onInfo(String info) throws RemoteException {
        if (onInfo != null) onInfo.accept(info);
    }
}
