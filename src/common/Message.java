package common;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    public String from; // username
    public String displayName; // igname
    public String content;
    public String group;
    public LocalDateTime time;
    public boolean isFile;
    public String fileName;
    public byte[] fileData;

    public Message() {
        time = LocalDateTime.now();
    }

    public Message(String from, String displayName, String content, String group) {
        this.from = from;
        this.displayName = displayName;
        this.content = content;
        this.group = group;
        this.time = LocalDateTime.now();
        this.isFile = false;
    }
}
