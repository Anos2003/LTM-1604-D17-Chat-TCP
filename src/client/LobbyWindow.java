package client;

import common.interfaces.ChatServerInterface;
import common.model.User;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.List;

public class LobbyWindow {
    private ChatServerInterface server;
    private User user;

    public LobbyWindow(ChatServerInterface server, User user){
        this.server = server;
        this.user = user;
    }

    public void start(Stage stage) {
        ListView<String> groupList = new ListView<>();
        try {
            List<String> groups = server.getAvailableGroups();
            groupList.getItems().addAll(groups);
        } catch (Exception e) { e.printStackTrace(); }

        Button btnJoin = new Button("Join Group");

        btnJoin.setOnAction(e -> {
            String selected = groupList.getSelectionModel().getSelectedItem();
            if(selected != null) {
                try {
					new ChatWindow(server, user, selected).start(new Stage());
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                stage.close();
            }
        });

        VBox root = new VBox(10, new Label("Select a group:"), groupList, btnJoin);
        stage.setScene(new Scene(root, 300, 300));
        stage.setTitle("Lobby");
        stage.show();
    }
}
