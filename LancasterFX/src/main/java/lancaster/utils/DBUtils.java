package lancaster.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.*;

public class DBUtils {
    public static void loginUser(ActionEvent event, String username, String password) throws IOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String dbUsername = "in2033t44_a";
        String dbPassword = "wcYtgG2jphQ";
        try {
            connection = DriverManager.getConnection("jdbc:mysql://sst-stuproj00:3306/in2033t444", dbUsername, dbPassword);
            preparedStatement = connection.prepareStatement("SELECT password, favChannel FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                // check if the username exists in the db
                System.out.println("User not found in db");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrievePassword = resultSet.getString("password");
                    String retrieveFavChannel = resultSet.getString("favChannel");
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
