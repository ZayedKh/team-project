package com.lancaster.ui;

import com.lancaster.utils.DBUtils;
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
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
//        try {
//            DBUtils.loginUser("jdbc:mysql://sst-stuproj00:3306/in2033t44", "Admin", "Admin");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        launch(args);
    }
}