package client.fx;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashUI {
    public void show() {
        Stage s = MainApp.primaryStage;
        Label title = new Label("🎧 Neon Chat");
        title.getStyleClass().add("neon-title");
        VBox root = new VBox(16, title);
        root.setAlignment(Pos.CENTER);
        Scene sc = new Scene(root, 520, 360);
        sc.getStylesheets().add(getClass().getResource("/neon.css").toExternalForm());
        s.setScene(sc);
        s.show();

        PauseTransition pt = new PauseTransition(Duration.seconds(1.2));
        pt.setOnFinished(e -> new LoginUI().show());
        pt.play();
    }
}
