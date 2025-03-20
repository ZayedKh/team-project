package com.lancaster.ui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.YearMonth;

public class LoginWindow extends Application {

    final int width = 1000;
    final int height = 800;
    final int textFieldWidth = 200;
    final int buttonWidth = 100;

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #122023;");
        Scene scene = new Scene(root, width, height);

        root.getChildren().add(createLoginUI());

        stage.setResizable(false);
        stage.setTitle("Lancaster Interface");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createLoginUI() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        StackPane headerStack = new StackPane();
        Rectangle header = createRectangle();
        Text welcomeBanner = createWelcomeBannerText();
        headerStack.getChildren().addAll(header, welcomeBanner);

        // Ensure the image path is correct
        Image logo = new Image("file:E:\\team-project\\Code\\LancasterFX\\src\\main\\resources\\com\\lancaster\\LancasterLogo.png");
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(200);
        logoView.setFitHeight(200);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefWidth(textFieldWidth);
        passwordField.setMaxWidth(textFieldWidth);

        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(buttonWidth);
        loginButton.setOnAction(e -> handleLogin(passwordField.getText()));

        layout.getChildren().addAll(logoView, headerStack, passwordField, loginButton);
        return layout;
    }

    private void handleLogin(String password) {
        if (!password.isEmpty()) {
            FullCalendarView calendarView = new FullCalendarView(YearMonth.now());
            primaryStage.getScene().setRoot(calendarView.getView());
        }
    }

    private Text createWelcomeBannerText() {
        Text welcomeBanner = new Text("Welcome to Lancaster's Employee Interface");
        welcomeBanner.setFont(new Font("Cambria", 24));
        welcomeBanner.setFill(Color.WHITE);
        return welcomeBanner;
    }

    private Rectangle createRectangle() {
        Rectangle rectangle = new Rectangle(width, 50);
        rectangle.setFill(Color.web("#2ECC40"));
        return rectangle;
    }

    public static void main(String[] args) {
        Application.launch();
    }
}