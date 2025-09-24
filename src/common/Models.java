package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Models {
    public static class User implements Serializable {
        public String username;
        public String password;
        public User() {}
        public User(String username, String password) { this.username = username; this.password = password; }
    }

    public static class ChatGroup implements Serializable {
        public String name;
        public List<String> members = new ArrayList<>();
        public ChatGroup() {}
        public ChatGroup(String name) { this.name = name; }
    }
}
