package client.fx;

import common.ChatService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class LobbyUI {
    private final ChatService service;
    private final String username;

    // Constructor gốc
    public LobbyUI(ChatService service, String username) {
        this.service = service;
        this.username = username;
    }

    // Constructor mới để tương thích với ChatRoomUI khi thoát phòng
    public LobbyUI(ChatService service, String gname, String username) {
        this(service, username); // gọi lại constructor gốc
        // gname không cần dùng ở lobby, nên bỏ qua
    }

    public void show() {
        Stage st = MainApp.primaryStage;
        Label title = new Label("Sảnh chờ - Chọn nhóm");
        title.getStyleClass().add("neon-title");

        ListView<String> lv = new ListView<>();
        try {
            List<String> groups = service.getGroups();
            lv.setItems(FXCollections.observableArrayList(groups));
        } catch (Exception e) {
            e.printStackTrace();
            lv.setItems(FXCollections.observableArrayList("General", "Sports", "Tech"));
        }

        TextField newGroup = new TextField();
        newGroup.setPromptText("Tạo nhóm mới");

        Button btnCreate = new Button("Tạo");
        btnCreate.setOnAction(ev -> {
            String g = newGroup.getText().trim();
            if (g.isEmpty()) return;
            try {
                boolean ok = service.createGroup(g);
                if (ok) {
                    lv.getItems().add(g);
                    newGroup.clear();
                } else {
                    showAlert("Nhóm đã tồn tại.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Lỗi khi tạo nhóm: " + ex.getMessage());
            }
        });

        Button btnJoin = new Button("Tham gia");
        btnJoin.setOnAction(ev -> {
            String grp = lv.getSelectionModel().getSelectedItem();
            if (grp == null) { showAlert("Chọn nhóm!"); return; }
            TextInputDialog dlg = new TextInputDialog(username);
            dlg.setHeaderText("Vui lòng nhập igname");
            dlg.showAndWait().ifPresent(igname -> {
                try {
                    boolean ok = service.joinGroup(username, grp, igname);
                    if (ok) {
                        new ChatRoomUI(service, username, grp, igname).show();
                    } else {
                        showAlert("Không thể tham gia nhóm.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert("Lỗi khi join nhóm: " + ex.getMessage());
                }
            });
        });

        VBox root = new VBox(12, title, lv, newGroup, btnCreate, btnJoin);
        root.setPadding(new Insets(12));
        Scene sc = new Scene(root, 600, 480);
        sc.getStylesheets().add(getClass().getResource("/neon.css").toExternalForm());
        st.setScene(sc);
        st.show();
    }

    private void showAlert(String t) {
        new Alert(Alert.AlertType.INFORMATION, t, ButtonType.OK).showAndWait();
    }
}
