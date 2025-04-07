package lancaster.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import lancaster.model.Booking;
import lancaster.model.RevenueCalculator;
import lancaster.model.RevenueCalculator.*;
import lancaster.model.RevenueEntry;
import lancaster.model.BookingType;
import lancaster.model.VenueSpace;


import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBUtils {
    private static Connection connection;
    private RevenueCalculator revenueCalculator;
    private VenueSpace venueSpace;
    private BookingType bookingType;

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

        revenueCalculator = new RevenueCalculator();
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

    public List<RevenueEntry> getBookingRevenueData(LocalDate fromDate, LocalDate toDate) throws SQLException {
        String query = """
        SELECT 
            r.room_name AS venue,
            e.event_date AS date,
            e.start_time,
            e.end_time,
            b.customer_name,
            sc.configuration_name
        FROM events e
        JOIN rooms r ON e.room_id = r.room_id
        JOIN bookings b ON e.booking_id = b.booking_id
        JOIN seating_configurations sc ON e.seating_config_id = sc.seating_config_id
        WHERE e.event_date BETWEEN ? AND ?
        ORDER BY e.event_date;
    """;

        List<RevenueEntry> revenueData = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(fromDate));
            stmt.setDate(2, Date.valueOf(toDate));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String venue = rs.getString("venue");
                LocalDate eventDate = rs.getDate("date").toLocalDate();
                String dateStr = eventDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                // Determine booking type
                LocalTime startTime = rs.getTime("start_time").toLocalTime();
                LocalTime endTime = rs.getTime("end_time").toLocalTime();
                String bookingTypeStr = determineBookingType(startTime, endTime);

                // Convert venue name to VenueSpace enum
                VenueSpace venueSpace = mapVenueNameToEnum(venue);

                // Convert booking type string to enum
               BookingType bookingType = BookingType.valueOf(bookingTypeStr);

                // Calculate room rate using the RevenueCalculator
                double roomRate = 0.0;
                if (venueSpace != null && bookingType != null) {
                    roomRate = revenueCalculator.calculateRoomRevenue(venueSpace, bookingType, false);
                }

                revenueData.add(new RevenueEntry(venue, dateStr, bookingTypeStr, roomRate, 0.0));
            }

            return revenueData;
        }
    }

    private VenueSpace mapVenueNameToEnum(String venueName) {
        return switch (venueName) {
            case "Green Room" -> VenueSpace.GREEN_ROOM;
            case "Bronte Boardroom" -> VenueSpace.BRONTE_BOARDROOM;
            case "Dickens Den" -> VenueSpace.DICKENS_DEN;
            case "Poe Parlor" -> VenueSpace.POE_PARLOR;
            case "Globe Room" -> VenueSpace.GLOBE_ROOM;
            case "Chekhov Chamber" -> VenueSpace.CHEKHOV_CHAMBER;
            default -> null;
        };
    }

    private String determineBookingType(LocalTime startTime, LocalTime endTime) {
        long durationHours = java.time.Duration.between(startTime, endTime).toHours();

        if (durationHours >= 8) {
            return "FULL_DAY";
        } else if (startTime.isBefore(LocalTime.of(13, 0)) && endTime.isAfter(LocalTime.of(12, 0)) && endTime.isBefore(LocalTime.of(18, 0))) {
            return "MORNING_AFTERNOON";
        } else if (startTime.isAfter(LocalTime.of(17, 0)) || startTime.equals(LocalTime.of(17, 0))) {
            return "EVENING";
        } else if (durationHours >= 24 * 7) {
            return "WEEKLY";
        } else {
            return "HOURLY";
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