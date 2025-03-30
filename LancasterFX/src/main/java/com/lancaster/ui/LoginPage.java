// File: com/lancaster/ui/LoginPage.java
package com.lancaster.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginPage extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/lancaster/ui/logged-in.fxml"));
        primaryStage.setTitle("Lancaster FX");

        // Increase the window size to better fit desktop screens
        primaryStage.setScene(new Scene(root, 1024, 768));

        // Make the window start maximized for desktop use
        primaryStage.setMaximized(true);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}