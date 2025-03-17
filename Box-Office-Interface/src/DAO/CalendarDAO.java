package DAO;

import models.ConcreteEvent;
import interfaces.CalendarInterface;
import interfaces.DBConnect;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

/**
 * This class gets the data needed for a calendar and uses the calendar interface and
 * database interface methods for implementations
 */
public class CalendarDAO implements CalendarInterface, DBConnect {

    /**
     * This will get all events from a certain date in a database, a very similar
     * implementation is used in the sheet generation
     *
     * @param connection The connection to the database
     * @param date       The date being searched for
     * @return The list of events for the given date
     */
    @Override
    public ArrayList<ConcreteEvent> getEventForDate(Connection connection, LocalDateTime date) {
        ArrayList<ConcreteEvent> eventList = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE DATE(?) = DATE(startTime)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setTimestamp(1, Timestamp.valueOf(date));
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                eventList.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return eventList;
    }

    /**
     * Gets all the events in the current database, useful for a full list of
     * events
     *
     * @param connection The connection to the database
     * @return A list of all events in the database
     */
    @Override
    public ArrayList<ConcreteEvent> getAllEvents(Connection connection) {
        ArrayList<ConcreteEvent> eventList = new ArrayList<>();
        String sql = "SELECT * FROM events";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                eventList.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return eventList;
    }

    /**
     * This will get all events in the database in start time order, useful in testing
     *
     * @param connection The connection to the database
     * @return A list of all events in time order
     */
    @Override
    public ArrayList<ConcreteEvent> getMostRecentEvents(Connection connection) {
        ArrayList<ConcreteEvent> eventList = new ArrayList<>();
        String sql = "SELECT * FROM events ORDER BY startTime ASC";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                eventList.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return eventList;
    }

    /**
     * This will get all events within a given month, for future possibly change
     * to add year if needed
     *
     * @param connection The connection to the database
     * @param month      A time.Month variable i.e. Month.APRIL
     * @return A list of all events in month timeframe
     */
    @Override
    public ArrayList<ConcreteEvent> getEventByMonth(Connection connection, Month month) {
        ArrayList<ConcreteEvent> eventList = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE MONTH(startTime) = ? OR MONTH(endTime) = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, month.getValue());
            statement.setInt(2, month.getValue());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                eventList.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return eventList;
    }

    /**
     * Establishes a connection between the user and the operation database with the
     * correct url, username and password for data details
     * @param url The url of the mySQL database
     * @param username The username of the database
     * @param password The password of the database
     * @return A connection between the user and database
     */
    @Override
    public Connection connect(String url, String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the connection between the user and the database
     * +----------------------------------+
     * |  PLEASE CLOSE AFTER CONNECTING!! |
     * +----------------------------------+
     * @param connection The connection being used for connection
     */
    @Override
    public void disconnect(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Maps a row from the ResultSet to a concrete Event object.
     * @param rs The ResultSet to map.
     * @return The mapped Event object.
     * @throws SQLException If a database access error occurs.
     */
    private ConcreteEvent mapRow(ResultSet rs) throws SQLException {
        return new ConcreteEvent(
                rs.getString("eventID"),
                rs.getString("eventName"),
                rs.getTimestamp("startTime").toLocalDateTime(),
                rs.getTimestamp("endTime").toLocalDateTime(),
                rs.getString("eventType"),
                rs.getString("eventStatus"),
                rs.getString("eventVenue")
        );
    }
}