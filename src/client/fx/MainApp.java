package client.fx;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        MainApp.primaryStage = primaryStage;
        new SplashUI().show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
