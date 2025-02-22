package interfaces;

import models.SeatingConfiguration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface for providing seating configuration data.
 * Author: Mohib Ishrat
 * Version: 1.0
 * Date: 20/02/2025
 */
public interface SeatingConfigurationInterface {

    /**
     * Retrieves all seating configurations.
     * @param connection The connection to the database.
     * @return A list of all seating configurations.
     * @throws SQLException If a database access error occurs.
     */
    List<SeatingConfiguration> getAllSeatingConfigurations(Connection connection) throws SQLException;

    /**
     * Retrieves a seating configuration by event ID.
     * @param connection The connection to the database.
     * @param eventID The ID of the event.
     * @return The seating configuration for the specified event ID.
     * @throws SQLException If a database access error occurs.
     */
    SeatingConfiguration getSeatingConfigurationByEventID(Connection connection, String eventID) throws SQLException;

    /**
     * Adds a new seating configuration.
     * @param connection The connection to the database.
     * @param seatingConfiguration The seating configuration to add.
     * @return True if the seating configuration was added successfully, false otherwise.
     * @throws SQLException If a database access error occurs.
     */
    boolean addSeatingConfiguration(Connection connection, SeatingConfiguration seatingConfiguration) throws SQLException;

    /**
     * Deletes a seating configuration by event ID.
     * @param connection The connection to the database.
     * @param eventID The ID of the event.
     * @return True if the seating configuration was deleted successfully, false otherwise.
     * @throws SQLException If a database access error occurs.
     */
    boolean deleteSeatingConfiguration(Connection connection, String eventID) throws SQLException;

    /**
     * Updates an existing seating configuration.
     * @param connection The connection to the database.
     * @param eventID The ID of the event.
     * @param seatingConfiguration The updated seating configuration.
     * @return True if the seating configuration was updated successfully, false otherwise.
     * @throws SQLException If a database access error occurs.
     */
    boolean updateSeatingConfiguration(Connection connection, String eventID, SeatingConfiguration seatingConfiguration) throws SQLException;
}