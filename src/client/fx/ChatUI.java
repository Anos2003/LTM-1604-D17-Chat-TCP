package client.fx;

import common.Message;
import javafx.scene.control.TextArea;

import java.time.format.DateTimeFormatter;

public class ChatUI {
    public static void append(TextArea ta, Message m) {
        String name = m.displayName != null ? m.displayName : m.from;
        String time = m.time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String line = "[" + time + "] " + name + ": " + (m.isFile ? "[File] " + m.fileName : m.content);
        ta.appendText(line + "\n");
    }
}
