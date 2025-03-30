package com.lancaster.controller;

import com.lancaster.utils.DBUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.YearMonth;
import java.util.ResourceBundle;

import com.lancaster.ui.FullCalendarView;

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
                    DBUtils.loginUser(actionEvent, tf_password.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}