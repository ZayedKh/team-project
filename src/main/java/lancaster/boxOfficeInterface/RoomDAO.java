package lancaster.boxOfficeInterface;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Data Access Object (DAO) interface for room related operations.
 * <p>
 * This interface defines methods to retrieve room capacity and the last booking time for a given room.
 * Implementations of this interface should provide the underlying database operations to fetch the requested data.
 * </p>
 */
public interface RoomDAO {

    /**
     * Retrieves the capacity of the room identified by the specified room ID.
     *
     * @param connection the database connection to use for the query
     * @param roomID the unique identifier of the room
     * @return the capacity of the room as an int
     * @throws SQLException if a database access error occurs
     */
    int getRoomCapacity(Connection connection, int roomID) throws SQLException;

    /**
     * Retrieves the last booking time for the room identified by the specified room ID.
     *
     * @param connection the database connection to use for the query
     * @param roomId the unique identifier of the room
     * @return a {@link Timestamp} representing the last booking time of the room
     * @throws SQLException if a database access error occurs
     */
    Timestamp getLastBookingTime(Connection connection, int roomId) throws SQLException;
}
