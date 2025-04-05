package lancaster.boxOfficeInterface;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class EventDAOImpl implements EventDAO {

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

    public ResultSet getData(Connection conn, int eventID) throws SQLException {
        String query = "SELECT * FROM events WHERE event_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, eventID);
        return stmt.executeQuery();
    }
}