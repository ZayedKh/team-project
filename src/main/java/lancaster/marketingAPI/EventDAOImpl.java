package lancaster.marketingAPI;

import lancaster.model.Event;
import lancaster.model.EventStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventDAOImpl implements EventDAO {

    @Override
    public List<Event> getAllScheduledEvents(Connection connection) throws SQLException {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM events WHERE status = 'SCHEDULED'";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                events.add(mapRowToEvent(rs));
            }
        }
        return events;
    }

    @Override
    public Event getEventById(Connection connection, String eventId) throws SQLException {
        Event event = null;
        String query = "SELECT * FROM events WHERE event_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, eventId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    event = mapRowToEvent(rs);
                }
            }
        }
        return event;
    }


    @Override
    public List<Event> getUpcomingEvents(Connection connection) throws SQLException {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM events WHERE event_date >= NOW() AND event_date <= DATE_ADD(NOW(), INTERVAL 2 DAY)";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                events.add(mapRowToEvent(rs));
            }
        }
        return events;
    }

    private Event mapRowToEvent(ResultSet rs) throws SQLException {
        return new Event(
                rs.getString("event_id"),
                rs.getString("event_name"),
                rs.getDate("event_date").toLocalDate(),
                rs.getTime("start_time").toLocalTime(),
                rs.getTime("end_time").toLocalTime(),
                rs.getString("event_type"),
                EventStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("last_updated").toLocalDateTime()
        );
    }
}