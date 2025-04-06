package lancaster.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import lancaster.model.Booking;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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

        System.out.println("url: " + url);

        connection = DriverManager.getConnection(url, username, password);

        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    public List<Booking> generateDailySheets(LocalDate date) throws SQLException {
        String query = """
                    SELECT 
                        r.room_name,
                        e.event_date,
                        e.start_time,
                        e.end_time,
                        b.customer_name,
                        sc.configuration_name
                    FROM events e
                    JOIN rooms r ON e.room_id = r.room_id
                    JOIN bookings b ON e.booking_id = b.booking_id
                    JOIN seating_configurations sc ON e.seating_config_id = sc.seating_config_id
                    WHERE e.event_date = ?
                    ORDER BY r.room_name, e.start_time;
                """;

        List<Booking> sheet = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String roomName = rs.getString("room_name");
                LocalTime startTime = rs.getTime("start_time").toLocalTime();
                LocalTime endTime = rs.getTime("end_time").toLocalTime();
                String customerName = rs.getString("customer_name");
                String configName = rs.getString("configuration_name");
                sheet.add(new Booking(roomName, date, startTime, endTime, customerName, configName));
            }

            return sheet;
        }
    }

    public List<String> getRoomNames() {
        String query = "SELECT room_name FROM rooms;";
        List<String> roomNames = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String roomName = rs.getString("room_name");
                roomNames.add(roomName);
            }

            return roomNames;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving room names", e);
        }
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