package org.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("blockchain-view.fxml")));
        Scene scene = new Scene(view);
        stage.getIcons().add(new Image(String.valueOf(Objects.requireNonNull(getClass().getResource("logo.png")))));
        stage.setTitle("Blockchain");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}