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

    // active callbacks (user -> callback)
    private final Map<String, ClientCallback> callbacks = new ConcurrentHashMap<>();
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
    public void unregisterCallback(String username) throws RemoteException {
        callbacks.remove(username);
        System.out.println("[SERVER] Callback unregistered: " + username);
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

        if (igname != null && !igname.isEmpty()) {
            displayNames.put(username, igname);
        }

        String welcome = "Chào mừng " + displayNames.getOrDefault(username, username) +
                " đã gia nhập nhóm chat " + groupName;
        broadcastInfoToGroup(groupName, welcome);
        return true;
    }

    @Override
    public synchronized boolean leaveGroup(String username, String groupName) throws RemoteException {
        ChatGroup g = groups.get(groupName);
        if (g == null) return false;
        boolean existed = g.members.remove(username);
        if (!existed) return false;
        db.writeGroups(groups);

        String nameToShow = displayNames.getOrDefault(username, username);

        Message sysMsg = new Message("system", "SYSTEM",
                "Người dùng \"" + nameToShow + "\" đã rời phòng \"" + groupName + "\"", groupName);
        sysMsg.isSystem = true;

        broadcastToGroup(groupName, sysMsg);
        System.out.println("[SERVER] User " + username + " left group " + groupName);
        return true;
    }

    @Override
    public void sendTyping(String username, String groupName) throws RemoteException {
        String igname = displayNames.getOrDefault(username, username);
        String info = igname + " đang nhập...";
        broadcastInfoToGroup(groupName, info);
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

    @Override
    public synchronized List<String> getOnlineUsers() throws RemoteException {
        List<String> result = new ArrayList<>();
        for (String user : callbacks.keySet()) {
            String dn = displayNames.get(user);
            if (dn != null && !dn.isEmpty() && !dn.equals(user)) {
                result.add(dn + " (" + user + ")");
            } else {
                result.add(user);
            }
        }
        return result;
    }

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

    private void broadcastInfo(String info) {
        callbacks.forEach((user, cb) -> {
            try {
                cb.onInfo(info);
            } catch (Exception e) {
                callbacks.remove(user);
            }
        });
    }

    private void broadcastInfoToGroup(String groupName, String info) {
        ChatGroup g = groups.get(groupName);
        if (g == null) return;
        for (String member : new ArrayList<>(g.members)) {
            ClientCallback cb = callbacks.get(member);
            if (cb != null) {
                try {
                    cb.onInfo(info);
                } catch (Exception e) {
                    callbacks.remove(member);
                    System.out.println("[SERVER] Removed callback for " + member);
                }
            }
        }
    }
}
