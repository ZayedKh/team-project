package lancaster.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;

public class DBUtils {
    public static void loginUser(ActionEvent event, String password) throws IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found", e);
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String dbUsername = "in2033t44_a";
        String dbPassword = "wcYtgG2jphQ";
        String dbURL = "jdbc:mysql://sst-stuproj00:3306/in2033t44";
        try {
            connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
            preparedStatement = connection.prepareStatement("SELECT password FROM Account WHERE password = ?");
            preparedStatement.setString(1, password);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                // check if the password exists in the db
                System.out.println("Password not found in db");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrievePassword = resultSet.getString("password");
                    if (password.equals(retrievePassword)) {
                        System.out.println("User logged in, password: " + password);
                    } else {
                        // the password is wrong
                        System.out.println("Passwords do not match");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Provided credentials are incorrect");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}