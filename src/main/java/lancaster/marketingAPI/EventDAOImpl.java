package lancaster.marketingAPI;

import lancaster.model.Event;
import lancaster.model.EventStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link EventDAO} interface for marketing purposes.
 * <p>
 * This class provides methods to retrieve event data from the database such as all scheduled events,
 * a specific event by its identifier, and upcoming events.
 * </p>
 *
 * @event This class is part of the marketing API to query events based on their status and schedules.
 */
public class EventDAOImpl implements EventDAO {

    /**
     * Retrieves all events with a status of "SCHEDULED" from the database.
     *
     * @param connection the {@code Connection} object used for the database query
     * @return a {@code List} of {@link Event} objects representing all scheduled events
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Retrieves an event by its identifier.
     *
     * @param connection the {@code Connection} object used for the database query
     * @param eventId the unique identifier of the event as a {@code String}
     * @return the {@link Event} object corresponding to the specified event identifier, or {@code null} if not found
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Retrieves upcoming events scheduled from the current time up to two days in the future.
     *
     * @param connection the {@code Connection} object used for the database query
     * @return a {@code List} of {@link Event} objects representing upcoming events within the next two days
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Maps the current row of the given {@code ResultSet} to an {@link Event} object.
     * <p>
     * This is a helper method that extracts event details such as event ID, booking ID, room ID,
     * seating configuration ID, event name, date, start time, and end time.
     * </p>
     *
     * @param rs the {@code ResultSet} positioned at the current row of event data
     * @return an {@link Event} object populated with data from the current row
     * @throws SQLException if a database access error occurs while reading from the {@code ResultSet}
     */
    private Event mapRowToEvent(ResultSet rs) throws SQLException {
        return new Event(
                rs.getInt("event_id"),
                rs.getInt("booking_id"),
                rs.getInt("room_id"),
                rs.getInt("seating_config_id"),
                rs.getString("name"),
                rs.getDate("event_date"),
                rs.getTime("start_time"),
                rs.getTime("end_time")
        );
    }
}
