package com.lancaster.lancasterfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import javafx.scene.paint.*;

import java.io.IOException;

public class HelloApplication extends Application {

    int width = 1400;
    int height = 800;
    int rectangleXStartPos = 0;
    int rectangleYStartPos = 50;
    int rectangleWidth = width;
    int rectangleHeight = 60;
    int textFieldWidth = 350;

    @Override
    public void start(Stage stage) throws IOException {
        Group root = new Group();
        Scene scene = new Scene(root, width, height);

        Rectangle rectangle = new Rectangle(rectangleXStartPos, rectangleYStartPos, rectangleWidth, rectangleHeight);
        rectangle.setFill(Color.BLUE);

        Text welcomeBanner = new Text("Welcome to Lancaster's Employee Interface");
        welcomeBanner.setFont(new Font("Cambria", 34));
        welcomeBanner.setFill(Color.WHITE);
        welcomeBanner.setTextOrigin(VPos.CENTER);
        welcomeBanner.setBoundsType(TextBoundsType.VISUAL);
        welcomeBanner.setX((width - welcomeBanner.getLayoutBounds().getWidth()) / 2);
        welcomeBanner.setY(40 + (60 + welcomeBanner.getLayoutBounds().getHeight()) / 2);

        Text passwordBanner = new Text("Enter your password:");
        passwordBanner.setFont(new Font("Cambria", 24));
        passwordBanner.setFill(Color.BLACK);
        passwordBanner.setX((width - passwordBanner.getLayoutBounds().getWidth())/2);
        passwordBanner.setY(height - 320);


        TextField textField = new TextField();
        textField.setPrefWidth(textFieldWidth);
        textField.setLayoutX((width - textFieldWidth)/2);
        textField.setLayoutY(height - 300);

        System.out.println("Text Field Width: " + textField.getWidth());


        System.out.println("Width of window: " + width + "\nWidth of text: " + welcomeBanner.getLayoutBounds().getWidth());
        System.out.println("Text x-position: " + welcomeBanner.getX() + "\nText y-position: " + welcomeBanner.getY());


        root.getChildren().add(rectangle);
        root.getChildren().add(welcomeBanner);
        root.getChildren().add(textField);
        root.getChildren().add(passwordBanner);
        stage.setResizable(false);
        stage.setTitle("Lancaster Interface");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        Application.launch();
    }
}