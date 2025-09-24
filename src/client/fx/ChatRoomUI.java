package client.fx;

import client.ClientCallbackImpl;
import common.ChatService;
import common.Message;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;

public class ChatRoomUI {
    private final ChatService service;
    private final String username;
    private final String group;
    private final String igname;
    private TextArea ta;
    private ClientCallbackImpl callbackImpl;

    public ChatRoomUI(ChatService service, String username, String group, String igname) {
        this.service = service;
        this.username = username;
        this.group = group;
        this.igname = igname;
    }

    public void show() {
        try {
            Stage st = MainApp.primaryStage;
            Label title = new Label("Group: " + group + " | Bạn: " + igname);
            title.getStyleClass().add("neon-title-small");

            ta = new TextArea();
            ta.setEditable(false);
            ta.setPrefHeight(360);

            // ✅ đổi chữ sang màu đen và nền trắng cho dễ nhìn
            ta.setStyle("-fx-text-fill: black; -fx-control-inner-background: white;");

            TextField tf = new TextField();
            tf.setPromptText("Nhập tin nhắn...");

            Button btnSend = new Button("Gửi");
            btnSend.setOnAction(e -> {
                String txt = tf.getText().trim();
                if (txt.isEmpty()) return;
                try {
                    Message m = new Message(username, igname, txt, group);
                    service.sendMessage(m);
                    tf.clear();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Lỗi gửi tin nhắn: " + ex.getMessage()).showAndWait();
                }
            });

            Button btnFile = new Button("Gửi file");
            btnFile.setOnAction(e -> {
                FileChooser fc = new FileChooser();
                File f = fc.showOpenDialog(st);
                if (f == null) return;
                try {
                    byte[] data = Files.readAllBytes(f.toPath());
                    Message m = new Message(username, igname, "[file]", group);
                    m.isFile = true;
                    m.fileName = f.getName();
                    m.fileData = data;
                    service.sendFile(m);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Lỗi gửi file: " + ex.getMessage()).showAndWait();
                }
            });

            VBox root = new VBox(10, title, ta, new VBox(6, tf, new VBox(6, btnSend, btnFile)));
            root.setPadding(new Insets(12));
            Scene sc = new Scene(root, 700, 520);
            sc.getStylesheets().add(getClass().getResource("/neon.css").toExternalForm());
            st.setScene(sc);
            st.show();

            // Đăng ký callback nhận tin nhắn
            callbackImpl = new ClientCallbackImpl(
                    m -> {
                        if (!m.group.equals(group)) return;
                        Platform.runLater(() -> {
                            ChatUI.append(ta, m);
                            if (m.isFile) {
                                try {
                                    File out = new File("downloads");
                                    out.mkdirs();
                                    File saved = new File(out, m.fileName);
                                    Files.write(saved.toPath(), m.fileData);
                                    ta.appendText("[SYSTEM] File saved to: " + saved.getAbsolutePath() + "\n");
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                    },
                    info -> Platform.runLater(() -> ta.appendText("[INFO] " + info + "\n"))
            );

            service.registerCallback(username, callbackImpl);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Lỗi ChatRoom: " + e.getMessage()).showAndWait();
        }
    }
}
