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
 * <p>
 * This class manages the login interface by handling the input of a username and password,
 * setting up event handlers for the login process, and interacting with the database to authenticate users.
 * </p>
 */
public class LogInController implements Initializable {

    @FXML
    private PasswordField tf_password;       // Field for entering the password

    @FXML
    private TextField tf_username;           // Field for entering the username

    @FXML
    private Button btn_login;                // Button to initiate the login process

    @FXML
    private Label label_welcome;             // Label to display a welcome message

    /**
     * Initializes the login controller after its root element has been completely processed.
     * <p>
     * This method performs the following initialization steps:
     * <ul>
     *   <li>Sets the focus to the welcome label after the application starts.</li>
     *   <li>Registers an action handler for the login button that validates input fields
     *       and delegates the authentication process to the {@link DBUtils#loginUser(ActionEvent, String, String)} method.</li>
     * </ul>
     * </p>
     *
     * @param url The location used to resolve relative paths for the root object, or {@code null} if unknown.
     * @param resourceBundle The resources used to localize the root object, or {@code null} if not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Request focus on the welcome label once the platform is ready.
        Platform.runLater(() -> label_welcome.requestFocus());

        // Set up the login button's action handler.
        btn_login.setOnAction((ActionEvent event) -> {
            try {
                // Verify that neither the username nor the password field is empty.
                if (!tf_password.getText().isEmpty() && !tf_username.getText().isEmpty()) {
                    // Instantiate DBUtils to perform database operations and attempt user login.
                    DBUtils dbUtils = new DBUtils();
                    dbUtils.loginUser(event, tf_username.getText(), tf_password.getText());
                } else {
                    // Display an error alert if either the username or password field is empty.
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Username/Password field is empty");
                    alert.show();
                }
            } catch (Exception e) {
                // Print the stack trace for debugging any exceptions during the login process.
                e.printStackTrace();
            }
        });
    }
}
