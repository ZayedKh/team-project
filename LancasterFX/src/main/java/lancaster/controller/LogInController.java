package lancaster.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.time.YearMonth;
import java.util.ResourceBundle;

import lancaster.ui.FullCalendarView;

public class LogInController implements Initializable {
    @FXML
    private TextField tf_password;
    @FXML
    private Button btn_login;
    @FXML
    private Label label_welcome;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> label_welcome.requestFocus());
        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    FullCalendarView calendarView = new FullCalendarView(YearMonth.now());
                    if (!tf_password.getText().isEmpty()) {
                        Stage primaryStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                        primaryStage.getScene().setRoot(calendarView.getView());
                        primaryStage.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}