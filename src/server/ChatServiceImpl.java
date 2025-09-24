package server;

import common.ChatService;
import common.ClientCallback;
import common.Message;
import common.Models.ChatGroup;
import common.Models.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServiceImpl extends UnicastRemoteObject implements ChatService {
    private static final long serialVersionUID = 1L;
    private final JSONDatabase db = new JSONDatabase();

    // persistent maps loaded from JSON
    private final Map<String, User> users;
    private final Map<String, ChatGroup> groups;

    // active callbacks
    private final Map<String, ClientCallback> callbacks = new ConcurrentHashMap<>();
    // last display name (igname) for a user
    private final Map<String, String> displayNames = new ConcurrentHashMap<>();

    protected ChatServiceImpl() throws RemoteException {
        super();
        users = db.loadUsers();
        groups = db.loadGroups();
    }

    @Override
    public synchronized boolean register(String username, String password) throws RemoteException {
        if (users.containsKey(username)) return false;
        users.put(username, new User(username, password));
        db.writeUsers(users);
        System.out.println("[SERVER] Registered user: " + username);
        return true;
    }

    @Override
    public synchronized boolean login(String username, String password) throws RemoteException {
        User u = users.get(username);
        boolean ok = u != null && u.password.equals(password);
        System.out.println("[SERVER] Login attempt: " + username + " -> " + ok);
        return ok;
    }

    @Override
    public void registerCallback(String username, ClientCallback cb) throws RemoteException {
        if (cb != null) {
            callbacks.put(username, cb);
            System.out.println("[SERVER] Callback registered: " + username);
        }
    }

    @Override
    public List<String> getGroups() throws RemoteException {
        return new ArrayList<>(groups.keySet());
    }

    @Override
    public synchronized boolean createGroup(String groupName) throws RemoteException {
        if (groups.containsKey(groupName)) return false;
        groups.put(groupName, new ChatGroup(groupName));
        db.writeGroups(groups);
        broadcastInfo("Nhóm mới được tạo: " + groupName);
        return true;
    }

    @Override
    public synchronized boolean joinGroup(String username, String groupName, String igname) throws RemoteException {
        ChatGroup g = groups.get(groupName);
        if (g == null) return false;
        if (!g.members.contains(username)) g.members.add(username);
        db.writeGroups(groups);
        displayNames.put(username, igname);
        String welcome = "Chào mừng " + igname + " đã gia nhập nhóm chat " + groupName;
        broadcastInfo(welcome);
        return true;
    }

    @Override
    public void sendMessage(Message m) throws RemoteException {
        if (displayNames.containsKey(m.from)) m.displayName = displayNames.get(m.from);
        broadcastToGroup(m.group, m);
    }

    @Override
    public void sendFile(Message m) throws RemoteException {
        if (displayNames.containsKey(m.from)) m.displayName = displayNames.get(m.from);
        m.isFile = true;
        broadcastToGroup(m.group, m);
    }

    // send message to members only
    private void broadcastToGroup(String groupName, Message m) {
        ChatGroup g = groups.get(groupName);
        if (g == null) return;
        for (String member : new ArrayList<>(g.members)) {
            ClientCallback cb = callbacks.get(member);
            if (cb != null) {
                try {
                    cb.onMessage(m);
                } catch (Exception e) {
                    callbacks.remove(member);
                    System.out.println("[SERVER] Removed callback for " + member);
                }
            }
        }
    }

    // broadcast info to all connected callbacks
    private void broadcastInfo(String info) {
        callbacks.forEach((user, cb) -> {
            try {
                cb.onInfo(info);
            } catch (Exception e) {
                callbacks.remove(user);
            }
        });
    }
}
