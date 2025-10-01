package client.fx;

import client.ClientCallbackImpl;
import common.ChatService;
import common.Message;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.nio.file.Files;

public class ChatRoomUI {
    private final ChatService service;
    private final String username;
    private final String group;
    private final String igname;
    private TextArea ta;
    private ClientCallbackImpl callbackImpl;
    private Label lblTyping;

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
            ta.setStyle("-fx-text-fill: black; -fx-control-inner-background: white;");

            TextField tf = new TextField();
            tf.setPromptText("Nhập tin nhắn...");

            lblTyping = new Label();
            lblTyping.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");

            Button btnSend = new Button("Gửi");
            btnSend.setOnAction(e -> sendMessage(tf));

            Button btnFile = new Button("Gửi file");
            btnFile.setOnAction(e -> sendFile(st));

            Button btnExit = new Button("Thoát phòng");
            btnExit.setOnAction(e -> {
                try {
                    service.leaveGroup(username, group);
                    service.unregisterCallback(username);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                new LobbyUI(service, username, igname).show();
            });

            HBox actions = new HBox(10, btnSend, btnFile, btnExit);
            VBox chatBox = new VBox(10, title, ta, tf, lblTyping, actions);
            chatBox.setPadding(new Insets(12));

            Label lblOnline = new Label("👥 Người dùng online");
            ListView<String> lvOnline = new ListView<>();
            lvOnline.setPrefWidth(160);
            lvOnline.setPrefHeight(400);

            Button btnRefresh = new Button("🔄 Làm mới");
            btnRefresh.setOnAction(e -> {
                try {
                    lvOnline.getItems().setAll(service.getOnlineUsers());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            VBox onlineBox = new VBox(8, lblOnline, lvOnline, btnRefresh);
            onlineBox.setPadding(new Insets(12));

            BorderPane root = new BorderPane();
            root.setCenter(chatBox);
            root.setRight(onlineBox);

            Scene sc = new Scene(root, 880, 520);
            sc.getStylesheets().add(getClass().getResource("/neon.css").toExternalForm());
            st.setScene(sc);
            st.show();

            callbackImpl = new ClientCallbackImpl(
                    m -> {
                        if (!m.group.equals(group)) return;
                        Platform.runLater(() -> {
                            if (m.isSystem) {
                                ta.appendText("[SYSTEM] " + m.content + "\n");
                            } else {
                                ChatUI.append(ta, m);
                            }
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
                            lblTyping.setText("");
                        });
                    },
                    info -> Platform.runLater(() -> {
                        if (info != null && info.contains("đang nhập")) {
                            lblTyping.setText(info);
                            PauseTransition pt = new PauseTransition(Duration.seconds(3));
                            pt.setOnFinished(ev -> lblTyping.setText(""));
                            pt.play();
                        } else {
                            ta.appendText("[INFO] " + info + "\n");
                        }
                    })
            );

            service.registerCallback(username, callbackImpl);

            tf.setOnKeyTyped(e -> {
                String ch = e.getCharacter();
                if (!ch.equals("\r") && !ch.equals("\n")) {
                    lblTyping.setText(igname + " đang nhập tin nhắn...");
                    try {
                        service.sendTyping(username, group);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    PauseTransition pt = new PauseTransition(Duration.seconds(2));
                    pt.setOnFinished(ev -> lblTyping.setText(""));
                    pt.play();
                }
            });

            tf.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    sendMessage(tf);
                    e.consume();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Lỗi ChatRoom: " + e.getMessage()).showAndWait();
        }
    }

    private void sendMessage(TextField tf) {
        String txt = tf.getText().trim();
        if (txt.isEmpty()) return;
        try {
            Message m = new Message(username, igname, txt, group);
            service.sendMessage(m);
            tf.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Lỗi gửi tin nhắn: " + ex.getMessage()).showAndWait();
        }
    }

    private void sendFile(Stage st) {
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
            new Alert(Alert.AlertType.ERROR,
                    "Lỗi gửi file: " + ex.getMessage()).showAndWait();
        }
    }
}
