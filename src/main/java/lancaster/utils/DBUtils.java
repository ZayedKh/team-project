package lancaster.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import lancaster.model.Booking;
import lancaster.model.Event;
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


/**
 * The {@code DBUtils} class creates utilities for the code that require the database
 * <p>
 * This class uses methods that will require a {@code connection} to the database via JDBC. The class itself
 * will create a {@code connection} when constructed and will use that connection until closed in some
 * certain methods.
 * </p>
 *
 */
public class DBUtils {
    private static Connection connection; //Connection to the database

    /**
     * Constructor for DBUtils that creates a connection between the user and the database
     * @throws SQLException             If a database access error occurs
     * @throws IOException              If an error occurs reading from input stream
     * @throws ClassNotFoundException   If the jdbc class cannot be found
     */
    public DBUtils() throws SQLException, IOException, ClassNotFoundException {
        Properties props = new Properties();

        //read from config file to get correct database username and password
        try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
            props.load(fis);
        }

        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");


        connection = DriverManager.getConnection(url, username, password);

        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    /**
     * This generates a daily sheet of data from the database
     * @param date          The date of the daily sheet
     * @return              A list of bookings being held that day
     * @throws SQLException If there is an error in either database access or table names have changed
     */
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
                //get all table columns and create a new booking object to be added to the list
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

    /**
     * This will work out if there is an event between specified time and date (used in apis)
     * @param date          The date of the event being scheduled
     * @param startTime     The start time of the scheduled event
     * @param endTime       The end time of the scheduled event
     * @return              Return if there is an event at that time
     * @throws SQLException If there is an error in connection or table names have changed
     */
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
                //return true if there is a value found
                return rs.getInt(1) > 0;
            }
        }

        return false;
    }

    /**
     * This will get the list of all the room names in the database
     * @return  List of strings of the room names
     */
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


    /**
     * This will log in a user if inserted credentials are correct, if not alert an error
     * @param event         The event of the button press, used to get window to change
     * @param username      The inputted username to be checked in the database
     * @param password      The inputted password to be checked in the database
     * @throws IOException  If there is an error loading the FXML
     */
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
                        // Relocate the user to the selection page
                        FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource("/lancaster/ui/selectionPane.fxml"));
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

    /**
     * This will create a new booking entry into the database
     * @param roomID        The ID of the room the event will take place
     * @param startDate     The start date of the booking
     * @param endDate       The end date of the booking
     * @param clientName    The name of the client booking the event/s
     * @param clientEmail   The email address of the client booking the event/s
     * @param clientPhone   The telephone number of the client booking the event/s
     * @param clientAddress The address of the client booking the event/s
     * @param status        The status of the booking, in default should be "pending"
     */
    public void createBooking(int roomID, Date startDate, Date endDate, String clientName, String clientEmail, String clientPhone, String clientAddress, String status) {
        String query = """
                        INSERT INTO bookings (booking_id, room_id, start_date,
                         end_date, customer_name, customer_email, customer_phone, customer_address, booking_status)
                        VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?)
                        """;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomID);
            statement.setDate(2, startDate);
            statement.setDate(3, endDate);
            statement.setString(4, clientName);
            statement.setString(5, clientEmail);
            statement.setString(6, clientPhone);
            statement.setString(7, clientAddress);
            statement.setString(8, status);

            statement.execute();
        }
        catch (SQLException e){
            throw new RuntimeException("Error creating booking");
        }

    }

    /**
     * This will create a new event entry into the database
     * @param roomID            The room ID the event is taking place
     * @param seating_configID  The seating configuration ID of the room for the event
     * @param name              The name of the event - what is the event
     * @param eventDate         The date of the event
     * @param startTime         The start time of the event
     * @param endTime           The end time of the event
     */
    public void createEvent(int roomID, int seating_configID, String name, Date eventDate,
                            Time startTime, Time endTime){
        String query = """
                    INSERT INTO events (event_id, booking_id, room_id, seating_config_id, Name, event_date, start_time, end_time)
                    VALUES(null, null, ?, ?, ?, ?, ?, ?)
                """;

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomID);
            statement.setInt(2, seating_configID);
            statement.setString(3, name);
            statement.setDate(4, eventDate);
            statement.setTime(5, startTime);
            statement.setTime(6, endTime);

            statement.execute();
            //close the connection after a booking and event are added
            connection.close();
        }
        catch(SQLException e){
            throw new RuntimeException("Error creating event");
        }
    }

    /**
     * This checks if there will be a conflict when trying to make a new booking
     * @param eventDate     The date of the event being booked
     * @param startTime     The start time of the event being booked
     * @param endTime       The end time of the event being booked
     * @param room_id       The ID of the room being booked
     * @return              A boolean of whether there is a conflict
     */
    public boolean bookingConflict(Date eventDate, Time startTime, Time endTime, int room_id){
        String query = """
                    SELECT event_id FROM events
                    WHERE event_date = ?
                    AND start_time <= ?
                    AND end_time >= ?
                    AND room_id = ?
                """;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, eventDate);
            statement.setTime(2, endTime);
            statement.setTime(3, startTime);
            statement.setInt(4, room_id);

            ResultSet rs = statement.executeQuery();

            //return true if there is a conflict
            return rs.isBeforeFirst();

        } catch (SQLException e) {
            throw new RuntimeException("Error checking booking clash");
        }


    }

    /**
     * This will get all the reviews in the database to display on screen
     * @return                  A list of all the reviews
     * @throws SQLException     If there is an error in connection or table name
     */
    public ArrayList<Review> getReviews() throws SQLException {
        ArrayList<Review> reviews = new ArrayList<>();
        String query = """
                    SELECT * FROM Review
                """;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                //should use table names but lazy
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

    /**
     * This will get the prices for a room for the room name
     * @param name  Name of room getting price for
     * @return      The price of the room per hour
     */
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

    /**
     * Gets all the events in a day
     * @param date  Day being searched for
     * @return      List of events on given date
     */
    public ArrayList<Event> getEventForDay(Date date){
        ArrayList<Event> events = new ArrayList<>();
        String query = """
                    SELECT * FROM events
                    WHERE event_date = ?
                """;

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, date);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                events.add(new Event(rs.getInt("event_id"),
                        rs.getInt("booking_id"),
                        rs.getInt("room_id"),
                        rs.getInt("seating_config_id"),
                        rs.getString("name"),
                        rs.getDate("event_date"),
                        rs.getTime("start_time"),
                        rs.getTime("end_time")));
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error getting daily events");
        }
        return events;
    }


    /**
     * This will get the room name based on it's ID value
     * @param room_id   The ID of the room being searched
     * @return          The name of the room
     */
    public String getRoomName(int room_id){
        String name = null;
        String query = """
                    SELECT room_name FROM rooms
                    WHERE room_id = ?
                """;

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, room_id);

            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                name = rs.getString("room_name");
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error getting room name");
        }
        return name;
    }

    /**
     * This will get the room ID based on the room name
     * @param room_name The room name being searched
     * @return          The ID value of the room
     */
    public int getRoomId(String room_name){
        int ID = 0;
        String query = """
                    SELECT room_id FROM rooms
                    WHERE room_name = ?
                """;


        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, room_name);

            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                ID = rs.getInt("room_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting room name");
        }
        return ID;
    }

}