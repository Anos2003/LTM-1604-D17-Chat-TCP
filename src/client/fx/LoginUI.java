package client.fx;

import common.ChatService;
import client.ClientCallbackImpl;
import common.Message;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.rmi.Naming;

public class LoginUI {

    public void show() {
        Stage s = MainApp.primaryStage;
        
        Label title = new Label("Đăng nhập");
        title.getStyleClass().add("neon-title");

        TextField tfUser = new TextField();
        tfUser.setPromptText("Username");
        PasswordField pf = new PasswordField();
        pf.setPromptText("Password");

        Button btnLogin = new Button("Login");
        Button btnRegister = new Button("Register");

        btnRegister.setOnAction(e -> new RegisterUI().show());

        btnLogin.setOnAction(ev -> {
            String u = tfUser.getText().trim();
            String p = pf.getText();
            if (u.isEmpty() || p.isEmpty()) {
                showAlert("Nhập username và password.");
                return;
            }
            try {
                ChatService svc = (ChatService) Naming.lookup("rmi://localhost:1099/ChatService");
                boolean ok = svc.login(u, p);
                if (ok) {
                    // register callback for server push
                    ClientCallbackImpl cb = new ClientCallbackImpl(
                            m -> {
                                // no global UI at this point; lobby/chat rooms register their own callbacks later
                                System.out.println("[CALLBACK] msg: " + m.content);
                            },
                            info -> {
                                System.out.println("[INFO] " + info);
                            }
                    );
                    svc.registerCallback(u, cb);

                    // go to lobby - pass svc and username
                    new LobbyUI(svc, u).show();
                } else {
                    showAlert("Sai username hoặc password.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Lỗi kết nối server: " + ex.getMessage());
            }
        });

        VBox root = new VBox(12, title, tfUser, pf, new VBox(10, btnLogin, btnRegister));
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        Scene sc = new Scene(root, 480, 380);
        sc.getStylesheets().add(getClass().getResource("/neon.css").toExternalForm());
        s.setScene(sc);
        s.show();
    }

    private void showAlert(String t) {
        new Alert(Alert.AlertType.INFORMATION, t, ButtonType.OK).showAndWait();
    }
}
