package com.lancaster.view;

import javafx.application.Application;
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

public class LoginWindow extends Application {

    final int width = 1400;
    final int height = 800;
    final int rectangleXStartPos = 0;
    final int rectangleYStartPos = 50;
    final int rectangleWidth = width;
    final int rectangleHeight = 60;
    final int textFieldWidth = 350;

    @Override
    public void start(Stage stage) throws IOException {
        Group root = new Group();
        Scene scene = new Scene(root, width, height);


        extracted(root);

        stage.setResizable(false);
        stage.setTitle("Lancaster Interface");
        stage.setScene(scene);
        stage.show();
    }

    private void extracted(Group root) {
        root.getChildren().add(createRectangle());
        root.getChildren().add(creatWelcomeBannerText());
        root.getChildren().add(createPasswordTextField());
        root.getChildren().add(createPasswordBannerText());
    }

    private TextField createPasswordTextField() {
        TextField textField = new TextField();
        textField.setPrefWidth(textFieldWidth);
        textField.setLayoutX((width - textFieldWidth) / 2);
        textField.setLayoutY(height - 300);
        return textField;
    }

    private Text createPasswordBannerText() {
        Text passwordBanner = new Text("Enter your password:");
        passwordBanner.setFont(new Font("Cambria", 24));
        passwordBanner.setFill(Color.BLACK);
        passwordBanner.setX((width - passwordBanner.getLayoutBounds().getWidth()) / 2);
        passwordBanner.setY(height - 320);
        return passwordBanner;
    }

    private Text creatWelcomeBannerText() {
        Text welcomeBanner = new Text("Welcome to Lancaster's Employee Interface");
        welcomeBanner.setFont(new Font("Cambria", 34));
        welcomeBanner.setFill(Color.WHITE);
        welcomeBanner.setTextOrigin(VPos.CENTER);
        welcomeBanner.setBoundsType(TextBoundsType.VISUAL);
        welcomeBanner.setX((width - welcomeBanner.getLayoutBounds().getWidth()) / 2);
        welcomeBanner.setY(40 + (60 + welcomeBanner.getLayoutBounds().getHeight()) / 2);
        return welcomeBanner;
    }

    public static void main(String[] args) {
        Application.launch();
    }

    private Rectangle createRectangle() {
        Rectangle rectangle = new Rectangle(rectangleXStartPos, rectangleYStartPos, rectangleWidth, rectangleHeight);
        rectangle.setFill(Color.BLUE);
        return rectangle;
    }
}