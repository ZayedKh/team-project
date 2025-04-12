package lancaster.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lancaster.utils.DBUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for handling user login in the Lancaster application.
 * This class manages the login interface, including username and password input,
 * and interacts with the database to authenticate users.
 */
public class LogInController implements Initializable {

    // **UI Elements**
    @FXML
    private PasswordField tf_password;       // Field for entering the password

    @FXML
    private TextField tf_username;           // Field for entering the username

    @FXML
    private Button btn_login;                // Button to initiate the login process

    @FXML
    private Label label_welcome;             // Label to display a welcome message

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up the login button's action handler and focuses on the welcome label.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Ensure the welcome label gets focus when the application starts
        Platform.runLater(() -> label_welcome.requestFocus());

        // Set up the login button's action handler
        btn_login.setOnAction((ActionEvent event) -> {
            try {
                // Check if both username and password fields are not empty
                if (!tf_password.getText().isEmpty() && !tf_username.getText().isEmpty()) {
                    // Create a DBUtils instance to handle database operations
                    DBUtils dbUtils = new DBUtils();
                    // Attempt to log in the user with the provided credentials
                    dbUtils.loginUser(event, tf_username.getText(), tf_password.getText());
                } else {
                    // Display an error alert if either field is empty
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Username/Password field is empty");
                    alert.show();
                }
            } catch (Exception e) {
                // Print the stack trace for any exceptions that occur during login
                e.printStackTrace();
            }
        });
    }
}