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
 * Controller class for the login screen.
 * Handles user authentication and transitions to the main application if successful.
 */
public class LogInController implements Initializable {

    @FXML
    private PasswordField tf_password; // Input field for user password

    @FXML
    private TextField tf_username; // Input field for username

    @FXML
    private Button btn_login; // Login button

    @FXML
    private Label label_welcome; // Welcome label for user interface focus

    /**
     * Called automatically when the login screen is loaded.
     * Sets up the login button logic and field focus behavior.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set initial focus to the welcome label (for accessibility/navigation flow)
        Platform.runLater(() -> label_welcome.requestFocus());

        // Set action for login button
        btn_login.setOnAction((ActionEvent event) -> {
            try {
                // Validate that fields are not empty
                if (!tf_password.getText().isEmpty() && !tf_username.getText().isEmpty()) {
                    DBUtils dbUtils = new DBUtils();
                    dbUtils.loginUser(event, tf_username.getText(), tf_password.getText()); // Attempt login
                } else {
                    // Show error alert if fields are empty
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Username/Password field is empty");
                    alert.show();
                }
            } catch (Exception e) {
                e.printStackTrace(); // Log any unexpected exceptions
            }
        });
    }
}
