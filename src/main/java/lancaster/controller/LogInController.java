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

public class LogInController implements Initializable {

    @FXML
    private PasswordField tf_password;
    @FXML
    private TextField tf_username;
    @FXML
    private Button btn_login;
    @FXML
    private Label label_welcome;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> label_welcome.requestFocus());
        btn_login.setOnAction((ActionEvent event) -> {
            try {
                if (!tf_password.getText().isEmpty() && !tf_username.getText().isEmpty()) {
                    DBUtils dbUtils = new DBUtils();
                    dbUtils.loginUser(event, tf_username.getText(), tf_password.getText());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Username/Password field is empty");
                    alert.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
