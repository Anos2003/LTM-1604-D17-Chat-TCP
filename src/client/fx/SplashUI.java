package client.fx;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class SplashUI {
    private static final String[] ICONS = {"💬", "📱", "👥", "🔐", "🌐", "🎧", "⚡"};

    public void show() {
        Stage s = MainApp.primaryStage;
        s.setTitle("ChatRoom RMI 💬");

        // ===== Title chính =====
        Label title = new Label("💬 ChatRoom Rmi");
        title.getStyleClass().add("neon-title");

        // Hiệu ứng neon pulse cho title
        DropShadow glow1 = new DropShadow(20, Color.rgb(160, 255, 255, 0.55));
        DropShadow glow2 = new DropShadow(35, Color.rgb(160, 255, 255, 0.9));
        title.setEffect(glow1);

        Timeline pulse = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(title.effectProperty(), glow1)),
                new KeyFrame(Duration.seconds(1), new KeyValue(title.effectProperty(), glow2))
        );
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();

        // ===== Layout chính =====
        VBox centerBox = new VBox(16, title);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPrefSize(520, 360);

        Pane root = new Pane(centerBox);
        centerBox.layoutXProperty().bind(root.widthProperty().subtract(centerBox.widthProperty()).divide(2));
        centerBox.layoutYProperty().bind(root.heightProperty().subtract(centerBox.heightProperty()).divide(2));

        Scene sc = new Scene(root, 520, 360);
        sc.getStylesheets().add(getClass().getResource("/neon.css").toExternalForm());
        s.setScene(sc);
        s.show();

        // ===== Icon bay xung quanh =====
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            String icon = ICONS[random.nextInt(ICONS.length)];
            Label iconLabel = new Label(icon);
            iconLabel.getStyleClass().add("fly-icon"); // dùng class css thay vì inline

            double x = random.nextDouble() * 500;
            double y = random.nextDouble() * 340;
            iconLabel.setLayoutX(x);
            iconLabel.setLayoutY(y);

            root.getChildren().add(iconLabel);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(3 + random.nextInt(3)), iconLabel);
            tt.setByY(-40 - random.nextInt(30));

            FadeTransition ft = new FadeTransition(Duration.seconds(3 + random.nextInt(2)), iconLabel);
            ft.setFromValue(0.7);
            ft.setToValue(0.0);

            ParallelTransition ptAnim = new ParallelTransition(tt, ft);
            ptAnim.setCycleCount(Animation.INDEFINITE);
            ptAnim.play();
        }

        // ===== Chuyển sang LoginUI =====
        PauseTransition pt = new PauseTransition(Duration.seconds(1.5));
        pt.setOnFinished(e -> new LoginUI().show());
        pt.play();
    }
}
