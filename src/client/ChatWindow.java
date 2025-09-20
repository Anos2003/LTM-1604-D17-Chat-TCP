package client;

import common.interfaces.ChatClientInterface;
import common.interfaces.ChatServerInterface;
import common.model.Message;
import common.model.User;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatWindow extends UnicastRemoteObject implements ChatClientInterface {
    private ChatServerInterface server;
    private User user;
    private String groupName;
    private TextArea chatArea;

    public ChatWindow(ChatServerInterface server, User user, String groupName) throws RemoteException {
        this.server = server;
        this.user = user;
        this.groupName = groupName;
    }

    public void start(Stage stage) {
        chatArea = new TextArea();
        chatArea.setEditable(false);
        TextField input = new TextField();
        Button sendBtn = new Button("Send");

        sendBtn.setOnAction(e -> {
            try {
                String text = input.getText();
                if(!text.isEmpty()) {
                    server.sendMessage(groupName, new Message(user.getUsername(), text));
                    input.clear();
                }
            } catch(Exception ex) { ex.printStackTrace(); }
        });

        VBox root = new VBox(10, chatArea, input, sendBtn);
        root.setPadding(new Insets(10));

        try {
            server.joinGroup(groupName, user, this);
        } catch (Exception ex) { ex.printStackTrace(); }

        stage.setScene(new Scene(root, 400, 400));
        stage.setTitle("Chat - " + groupName);
        stage.show();
    }

    @Override
    public void receiveMessage(Message message) throws RemoteException {
        Platform.runLater(() -> chatArea.appendText(message.toString() + "\n"));
    }

    @Override
    public void notifyUserJoined(String groupName, String username) throws RemoteException {
        Platform.runLater(() -> chatArea.appendText(">> " + username + " joined " + groupName + "\n"));
    }
}
