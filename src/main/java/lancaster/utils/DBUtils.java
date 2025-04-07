package lancaster.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import lancaster.model.Booking;
import lancaster.model.Review;

import javax.imageio.plugins.jpeg.JPEGImageReadParam;
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
                        b.end_date,
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
                LocalDate endDate = rs.getDate("end_date").toLocalDate();
                LocalTime startTime = rs.getTime("start_time").toLocalTime();
                LocalTime endTime = rs.getTime("end_time").toLocalTime();
                String customerName = rs.getString("customer_name");
                String configName = rs.getString("configuration_name");
                sheet.add(new Booking(roomName, date, endDate, startTime, endTime, customerName, configName));
            }
        }

        return sheet;

    }

    public boolean isEventScheduled(LocalDate date, LocalTime startTime, LocalTime endTime) throws SQLException {
        String query = """
            SELECT COUNT(*) 
            FROM events 
            WHERE event_date = ? 
              AND start_time <= ? 
              AND end_time >= ?;
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(date));
            stmt.setTime(2, Time.valueOf(endTime));
            stmt.setTime(3, Time.valueOf(startTime));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }

        return false;
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

    public void createBooking(int roomID, Date startDate, Date endDate, String clientName, String status) {
        String query = """
                        INSERT INTO bookings (booking_id, room_id, start_date,
                         end_date, customer_name, booking_status)
                        VALUES (null, ?, ?, ?, ?, ?)
                        """;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomID);
            statement.setDate(2, startDate);
            statement.setDate(3, endDate);
            statement.setString(4, clientName);
            statement.setString(5, status);

            statement.execute();
            connection.close();
        }
        catch (SQLException e){
            throw new RuntimeException("Error creating booking");
        }

    }

    public void createEvent(int bookingID, int roomID, int seating_configID, Date eventDate,
                            Time startTime, Time endTime){
        String query = """
                    INSERT INTO events (event_id, booking_id,  room_id, seating_config_id, event_date, start_date, end_date)
                    VALUES(null, ?, ?, ?, ?, ?, ?)
                """;

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bookingID);
            statement.setInt(2, roomID);
            statement.setInt(3, seating_configID);
            statement.setDate(4, eventDate);
            statement.setTime(5, startTime);
            statement.setTime(6, endTime);
            statement.execute();
            connection.close();
        }
        catch(SQLException e){
            throw new RuntimeException("Error creating event");
        }
    }

    public boolean bookingConflict(Date eventDate, Time startTime, Time endTime){
        String query = """
                    SELECT event_id FROM events
                    WHERE event_date = ?
                    AND start_time >= ?
                    AND end_time <= ?
                """;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, eventDate);
            statement.setTime(2, startTime);
            statement.setTime(3, endTime);

            ResultSet rs = statement.executeQuery();

            return rs.isBeforeFirst();

        } catch (SQLException e) {
            throw new RuntimeException("Error checking booking clash");
        }


    }

    public ArrayList<Review> getReviews() throws SQLException {
        ArrayList<Review> reviews = new ArrayList<>();
        String query = """
                    SELECT * FROM Review
                """;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Review rev = new Review(rs.getInt(1), rs.getInt(2),
                        rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7), rs.getString(8));
                reviews.add(rev);
            }
        }
        catch (SQLException e){
            throw new RuntimeException("Error getting reviews");
        }
        connection.close();
        return reviews;
    }

    public int getRoomPrice(String name){
        String query = """
                       SELECT rp.price
                       FROM RoomPrice rp
                       JOIN rooms r ON rp.roomid = r.room_id
                       WHERE r.room_name = ?;
                       """;

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                return resultSet.getInt(1);
            }
        }
        catch (SQLException e){
            throw new RuntimeException("Error getting prices");
        }
        return 0;
    }


}