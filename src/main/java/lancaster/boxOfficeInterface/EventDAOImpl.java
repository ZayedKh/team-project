package lancaster.boxOfficeInterface;

import javafx.scene.layout.AnchorPane;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Implementation of the {@link EventDAO} interface.
 * <p>
 * This class provides methods to retrieve various event-related details from the database,
 * such as start and end times, event duration, room ID, event data, and the event start date.
 * </p>
 */
public class EventDAOImpl implements EventDAO {

    /**
     * Retrieves the start time of the specified event.
     *
     * @param conn    the database connection to be used for the query
     * @param eventID the unique identifier of the event
     * @return the start time of the event as a {@link LocalTime}, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public LocalTime getStartTime(Connection conn, int eventID) throws SQLException {
        String query = "SELECT event_date, start_time FROM events WHERE event_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, eventID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Time time = rs.getTime("start_time");
                return time.toLocalTime();
            }
        }
        return null;
    }

    /**
     * Retrieves the end time of the specified event.
     *
     * @param conn    the database connection to be used for the query
     * @param eventID the unique identifier of the event
     * @return the end time of the event as a {@link LocalTime}, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public LocalTime getEndTime(Connection conn, int eventID) throws SQLException {
        String query = "SELECT event_date, end_time FROM events WHERE event_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, eventID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Time time = rs.getTime("end_time");
                return time.toLocalTime();
            }
        }
        return null;
    }

    /**
     * Retrieves the start date of the specified event.
     *
     * @param conn    the database connection to be used for the query
     * @param eventID the unique identifier of the event
     * @return the start date of the event as a {@link LocalDate}, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public LocalDate getEventStartDate(Connection conn, int eventID) throws SQLException {
        String query = "SELECT event_date FROM events WHERE event_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, eventID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Date eventDate = rs.getDate("event_date");
                if (eventDate != null) {
                    return eventDate.toLocalDate();
                }
            }
        }
        return null;
    }

    /**
     * Retrieves the duration of the specified event in minutes.
     * <p>
     * The duration is calculated as the difference between the event's start and end times.
     * </p>
     *
     * @param conn    the database connection to be used for the query
     * @param eventID the unique identifier of the event
     * @return the duration of the event in minutes, or -1 if the event could not be found or calculated
     * @throws SQLException if a database access error occurs
     */
    public int getDuration(Connection conn, int eventID) throws SQLException {
        String query = "SELECT event_date, start_time, end_time FROM events WHERE event_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, eventID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Date eventDate = rs.getDate("event_date");
                Time startTime = rs.getTime("start_time");
                Time endTime = rs.getTime("end_time");

                if (eventDate != null && startTime != null && endTime != null) {
                    LocalDateTime startDateTime = LocalDateTime.of(eventDate.toLocalDate(), startTime.toLocalTime());
                    LocalDateTime endDateTime = LocalDateTime.of(eventDate.toLocalDate(), endTime.toLocalTime());

                    Duration duration = Duration.between(startDateTime, endDateTime);
                    return (int) duration.toMinutes();
                }
            }
        }
        return -1;
    }

    /**
     * Retrieves the room ID associated with the specified event.
     *
     * @param conn    the database connection to be used for the query
     * @param eventID the unique identifier of the event
     * @return the room ID as an int, or -1 if not found
     * @throws SQLException if a database access error occurs
     */
    public int getRoomID(Connection conn, int eventID) throws SQLException {
        String query = "SELECT room_id FROM events WHERE event_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, eventID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("room_id");
            }
        }
        return -1;
    }

    /**
     * Retrieves all data for the specified event.
     * <p>
     * The result is provided as a {@link ResultSet} containing all columns for the event.
     * </p>
     *
     * @param conn    the database connection to be used for the query
     * @param eventID the unique identifier of the event
     * @return a {@link ResultSet} containing the event data
     * @throws SQLException if a database access error occurs
     */
    public ResultSet getData(Connection conn, int eventID) throws SQLException {
        String query = "SELECT * FROM events WHERE event_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, eventID);
        return stmt.executeQuery();
    }
}
