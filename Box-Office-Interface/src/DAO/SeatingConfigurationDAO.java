package DAO;

import models.SeatingConfiguration;
import interfaces.DBConnect;
import interfaces.SeatingConfigurationInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for handling database operations related to seating configurations.
 */
public class SeatingConfigurationDAO implements SeatingConfigurationInterface, DBConnect {

    /**
     * Retrieves all seating configurations from the database.
     * @param connection The connection to the database.
     * @return A list of all seating configurations.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    public List<SeatingConfiguration> getAllSeatingConfigurations(Connection connection) throws SQLException {
        String query = "SELECT * FROM seating_configurations";
        List<SeatingConfiguration> seatingConfigurations = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                SeatingConfiguration seatingConfiguration = mapRow(resultSet);
                seatingConfigurations.add(seatingConfiguration);
            }
        }
        return seatingConfigurations;
    }

    /**
     * Retrieves a seating configuration by event ID from the database.
     * @param connection The connection to the database.
     * @param eventID The ID of the event.
     * @return The seating configuration for the specified event ID.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    public SeatingConfiguration getSeatingConfigurationByEventID(Connection connection, String eventID) throws SQLException {
        String query = "SELECT * FROM seating_configurations WHERE event_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, eventID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }
        return null;
    }

    /**
     * Adds a new seating configuration to the database.
     * @param connection The connection to the database.
     * @param seatingConfiguration The seating configuration to add.
     * @return True if the seating configuration was added successfully, false otherwise.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    public boolean addSeatingConfiguration(Connection connection, SeatingConfiguration seatingConfiguration) throws SQLException {
        String query = "INSERT INTO seating_configurations (event_id, total_seats, restricted_view_seats, accessible_seats, has_accessible_seating) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, seatingConfiguration.getEventID());
            statement.setInt(2, seatingConfiguration.getTotalSeats());
            statement.setInt(3, seatingConfiguration.getRestrictedViewSeats());
            statement.setInt(4, seatingConfiguration.getAccessibleSeats());
            statement.setBoolean(5, seatingConfiguration.hasAccessibleSeating());
            return statement.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a seating configuration by event ID from the database.
     * @param connection The connection to the database.
     * @param eventID The ID of the event.
     * @return True if the seating configuration was deleted successfully, false otherwise.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    public boolean deleteSeatingConfiguration(Connection connection, String eventID) throws SQLException {
        String query = "DELETE FROM seating_configurations WHERE event_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, eventID);
            return statement.executeUpdate() > 0;
        }
    }

    /**
     * Updates an existing seating configuration in the database.
     * @param connection The connection to the database.
     * @param eventID The ID of the event.
     * @param seatingConfiguration The updated seating configuration.
     * @return True if the seating configuration was updated successfully, false otherwise.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    public boolean updateSeatingConfiguration(Connection connection, String eventID, SeatingConfiguration seatingConfiguration) throws SQLException {
        String query = "UPDATE seating_configurations SET total_seats = ?, restricted_view_seats = ?, accessible_seats = ?, has_accessible_seating = ? WHERE event_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, seatingConfiguration.getTotalSeats());
            statement.setInt(2, seatingConfiguration.getRestrictedViewSeats());
            statement.setInt(3, seatingConfiguration.getAccessibleSeats());
            statement.setBoolean(4, seatingConfiguration.hasAccessibleSeating());
            statement.setString(5, eventID);
            return statement.executeUpdate() > 0;
        }
    }

    /**
     * Maps a row from the ResultSet to a SeatingConfiguration object.
     * @param resultSet The ResultSet to map.
     * @return The mapped SeatingConfiguration object.
     * @throws SQLException If a database access error occurs.
     */
    private SeatingConfiguration mapRow(ResultSet resultSet) throws SQLException {
        String eventID = resultSet.getString("event_id");
        int totalSeats = resultSet.getInt("total_seats");
        int restrictedViewSeats = resultSet.getInt("restricted_view_seats");
        int accessibleSeats = resultSet.getInt("accessible_seats");
        boolean hasAccessibleSeating = resultSet.getBoolean("has_accessible_seating");
        return new SeatingConfiguration(eventID, totalSeats, restrictedViewSeats, accessibleSeats, hasAccessibleSeating) {
            @Override
            public void updateSeatingConfiguration(int newTotalSeats, int newRestrictedSeats, int newAccessibleSeats) {
                // Implementation for updating seating configuration
            }
        };
    }

    /**
     * Establishes a connection to the database.
     * @param url The URL of the database.
     * @param username The username for the database.
     * @param password The password for the database.
     * @return The connection to the database.
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
     * Closes the connection to the database.
     * @param connection The connection to close.
     */
    @Override
    public void disconnect(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}