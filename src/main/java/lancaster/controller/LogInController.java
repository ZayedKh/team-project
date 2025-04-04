package lancaster.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    @FXML
    private PasswordField tf_password;
    @FXML
    private Button btn_login;
    @FXML
    private Label label_welcome;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> label_welcome.requestFocus());
        btn_login.setOnAction((ActionEvent event) -> {
            try {
                if (!tf_password.getText().isEmpty()) {
                    Stage primaryStage = (Stage) btn_login.getScene().getWindow();
                    // Adjust the resource path if needed:
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/lancaster/ui/SelectionPane.fxml"));
                    Parent selectionPane = loader.load();
                    primaryStage.getScene().setRoot(selectionPane);
                    primaryStage.show();
                } else {
                    System.out.println("Password field is empty.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
