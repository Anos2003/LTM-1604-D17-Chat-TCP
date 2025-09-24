package client.fx;

import common.ChatService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.rmi.Naming;

public class RegisterUI {
    public void show() {
        Stage s = MainApp.primaryStage;
        Label title = new Label("Đăng ký");
        title.getStyleClass().add("neon-title");

        TextField tfUser = new TextField();
        tfUser.setPromptText("Username");
        PasswordField pf = new PasswordField();
        pf.setPromptText("Password");
        PasswordField pf2 = new PasswordField();
        pf2.setPromptText("Confirm Password");

        Button btnReg = new Button("Đăng ký");
        Button btnBack = new Button("Quay lại");

        btnReg.setOnAction(ev -> {
            String u = tfUser.getText().trim();
            String p = pf.getText();
            String p2 = pf2.getText();
            if (u.isEmpty() || p.isEmpty()) {
                showAlert("Nhập đủ thông tin.");
                return;
            }
            if (!p.equals(p2)) {
                showAlert("Password nhập lại không khớp.");
                return;
            }
            try {
                ChatService svc = (ChatService) Naming.lookup("rmi://localhost:1099/ChatService");
                boolean ok = svc.register(u, p);
                if (ok) {
                    showAlert("Đăng ký thành công!");
                    new LoginUI().show();
                } else {
                    showAlert("Username đã tồn tại.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Lỗi kết nối server: " + ex.getMessage());
            }
        });

        btnBack.setOnAction(ev -> new LoginUI().show());

        VBox root = new VBox(10, title, tfUser, pf, pf2, btnReg, btnBack);
        root.setPadding(new Insets(16));
        root.setAlignment(Pos.CENTER);
        Scene sc = new Scene(root, 480, 420);
        sc.getStylesheets().add(getClass().getResource("/neon.css").toExternalForm());
        s.setScene(sc);
        s.show();
    }

    private void showAlert(String t) {
        new Alert(Alert.AlertType.INFORMATION, t, ButtonType.OK).showAndWait();
    }
}
