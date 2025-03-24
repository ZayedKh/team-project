package com.lancaster.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.YearMonth;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/lancaster/ui/log-in.fxml"));
        primaryStage.setTitle("Lancaster FX");
        primaryStage.setScene(new Scene(root, 536, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}