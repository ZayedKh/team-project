package lancaster.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBUtils {
    private static Connection connection;

    public DBUtils() throws SQLException, IOException, ClassNotFoundException {
        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
            props.load(fis);
        }

        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        connection = DriverManager.getConnection(url, username, password);

        Class.forName("com.mysql.cj.jdbc.Driver");
    }


    public void loginUser(ActionEvent event, String username, String password) throws IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found", e);
        }

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            preparedStatement = connection.prepareStatement("SELECT username, password FROM Account WHERE password = ? AND username = ?");
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, username);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("Credentials not found in db");
                connection.close();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrieveUsername = resultSet.getString("username");
                    String retrievePassword = resultSet.getString("password");
                    if (password.equals(retrievePassword) && username.equals(retrieveUsername)) {
                        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        // Adjust the resource path if needed:
                        FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource("/lancaster/ui/SelectionPane.fxml"));
                        Parent selectionPane = loader.load();
                        primaryStage.getScene().setRoot(selectionPane);
                        primaryStage.show();
                        connection.close();
                        break;
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