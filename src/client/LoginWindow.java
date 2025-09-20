package client;

import common.interfaces.ChatServerInterface;
import common.model.User;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.rmi.Naming;

public class LoginWindow {
    private ChatServerInterface server;

    public void start(Stage stage) {
        try {
            server = (ChatServerInterface) Naming.lookup("rmi://localhost/ChatServer");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Label lblUser = new Label("Username:");
        TextField txtUser = new TextField();
        Label lblPass = new Label("Password:");
        PasswordField txtPass = new PasswordField();
        Button btnLogin = new Button("Login");
        Button btnRegister = new Button("Register");

        VBox root = new VBox(10, lblUser, txtUser, lblPass, txtPass, btnLogin, btnRegister);
        root.setPadding(new Insets(20));

        btnLogin.setOnAction(e -> {
            try {
                User user = new User(txtUser.getText(), txtPass.getText());
                if(server.login(user, null)) {
                    new LobbyWindow(server, user).start(new Stage());
                    stage.close();
                } else {
                    showAlert("Login failed!");
                }
            } catch(Exception ex) { ex.printStackTrace(); }
        });

        btnRegister.setOnAction(e -> {
            try {
                User user = new User(txtUser.getText(), txtPass.getText());
                if(server.register(user)) {
                    showAlert("Register successful!");
                } else {
                    showAlert("User already exists!");
                }
            } catch(Exception ex) { ex.printStackTrace(); }
        });

        stage.setScene(new Scene(root, 300, 200));
        stage.setTitle("Login/Register");
        stage.show();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }
}
