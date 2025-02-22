package dao;

import classes.Event;
import enums.EventStatus;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import classes.ConcreteEvent;

/**
 * Data Access Object (DAO) class for managing Event entities in the database.
 */
public class EventDAO {
    private final Connection connection;

    /**
     * Constructs an EventDAO with the specified database connection.
     *
     * @param connection The database connection to be used by this DAO.
     */
    public EventDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Adds a new event to the database.
     *
     * @param event The event to be added.
     * @throws SQLException If a database access error occurs.
     */
    public void addEvent(Event event) throws SQLException {
        String sql = "INSERT INTO events (eventId, eventName, eventDateTime, eventType, status, lastUpdated) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, event.getEventId());
            stmt.setString(2, event.getEventName());
            stmt.setTimestamp(3, Timestamp.valueOf(event.getEventDateTime()));
            stmt.setString(4, event.getEventType());
            stmt.setString(5, event.getStatus().name());
            stmt.setTimestamp(6, Timestamp.valueOf(event.getLastUpdated()));
            stmt.executeUpdate();
        }
    }

    /**
     * Retrieves an event from the database by its unique identifier.
     *
     * @param eventId The unique identifier of the event.
     * @return The event object if found, otherwise null.
     * @throws SQLException If a database access error occurs.
     */
    public Event getEventById(String eventId) throws SQLException {
        String sql = "SELECT * FROM events WHERE eventId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, eventId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToEvent(rs);
            }
        }
        return null;
    }

    /**
     * Retrieves all events from the database.
     *
     * @return A list of all events.
     * @throws SQLException If a database access error occurs.
     */
    public List<Event> getAllEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                events.add(mapRowToEvent(rs));
            }
        }
        return events;
    }

    /**
     * Updates an existing event in the database.
     *
     * @param event The event with updated details.
     * @throws SQLException If a database access error occurs.
     */
    public void updateEvent(Event event) throws SQLException {
        String sql = "UPDATE events SET eventName = ?, eventDateTime = ?, eventType = ?, status = ?, lastUpdated = ? WHERE eventId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, event.getEventName());
            stmt.setTimestamp(2, Timestamp.valueOf(event.getEventDateTime()));
            stmt.setString(3, event.getEventType());
            stmt.setString(4, event.getStatus().name());
            stmt.setTimestamp(5, Timestamp.valueOf(event.getLastUpdated()));
            stmt.setString(6, event.getEventId());
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes an event from the database by its unique identifier.
     *
     * @param eventId The unique identifier of the event to be deleted.
     * @throws SQLException If a database access error occurs.
     */
    public void deleteEvent(String eventId) throws SQLException {
        String sql = "DELETE FROM events WHERE eventId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, eventId);
            stmt.executeUpdate();
        }
    }

    /**
     * Maps a row from the ResultSet to an Event object.
     *
     * @param rs The ResultSet containing event data.
     * @return The mapped Event object.
     * @throws SQLException If a database access error occurs.
     */
    private Event mapRowToEvent(ResultSet rs) throws SQLException {
        String eventId = rs.getString("eventId");
        String eventName = rs.getString("eventName");
        LocalDateTime eventDateTime = rs.getTimestamp("eventDateTime").toLocalDateTime();
        String eventType = rs.getString("eventType");
        EventStatus status = EventStatus.valueOf(rs.getString("status"));
        LocalDateTime lastUpdated = rs.getTimestamp("lastUpdated").toLocalDateTime();

        return new ConcreteEvent(eventId, eventName, eventDateTime, eventType, status, lastUpdated);
    }
}