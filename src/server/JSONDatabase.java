package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import common.Models.ChatGroup;
import common.Models.User;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class JSONDatabase {
    private static final String USERS = "data/users.json";
    private static final String GROUPS = "data/database.json";
    private final Gson gson = new Gson();

    public Map<String, User> loadUsers() {
        try {
            File f = new File(USERS);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                writeUsers(new HashMap<>());
            }
            Map<String, User> map = gson.fromJson(new FileReader(f), new TypeToken<Map<String, User>>() {}.getType());
            return map == null ? new HashMap<>() : map;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public void writeUsers(Map<String, User> users) {
        try (FileWriter w = new FileWriter(USERS)) {
            gson.toJson(users, w);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public Map<String, ChatGroup> loadGroups() {
        try {
            File f = new File(GROUPS);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                Map<String, ChatGroup> defaultGroups = new HashMap<>();
                defaultGroups.put("General", new ChatGroup("General"));
                defaultGroups.put("Sports", new ChatGroup("Sports"));
                defaultGroups.put("Tech", new ChatGroup("Tech"));
                writeGroups(defaultGroups);
            }
            Map<String, ChatGroup> map = gson.fromJson(new FileReader(f), new TypeToken<Map<String, ChatGroup>>() {}.getType());
            return map == null ? new HashMap<>() : map;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public void writeGroups(Map<String, ChatGroup> groups) {
        try (FileWriter w = new FileWriter(GROUPS)) {
            gson.toJson(groups, w);
        } catch (Exception e) { e.printStackTrace(); }
    }
}
