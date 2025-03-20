package lancaster.ui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.YearMonth;

public class LoginWindow extends Application {

    final int width = 600;
    final int height = 400;
    final int textFieldWidth = 250;

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        StackPane root = new StackPane();
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

        Rectangle header = createRectangle();
        Text welcomeBanner = createWelcomeBannerText();
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefWidth(textFieldWidth);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin(passwordField.getText()));

        layout.getChildren().addAll(header, welcomeBanner, passwordField, loginButton);
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
        rectangle.setFill(Color.GREY);
        return rectangle;
    }

    public static void main(String[] args) {
        Application.launch();
    }
}
