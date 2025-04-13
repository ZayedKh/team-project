package lancaster.marketingAPI;

import lancaster.model.Room;

import java.sql.Connection;
import java.util.List;

/**
 * The {@code RoomDAO} interface defines the operations for retrieving {@link Room} related data
 * from a data source. This abstraction allows for different implementations of room-related
 * data access, facilitating decoupling of business logic from persistence mechanisms.
 *
 * <p>
 * Implementations of this interface are expected to provide database-specific behavior for fetching
 * room details. The primary method defined in this interface retrieves a list of all rooms available
 * in the system given a valid database connection.
 * </p>
 *
 * This interface is part of the marketing API and is used for accessing room data.
 */
public interface RoomDAO {

    /**
     * Retrieves a list of all {@link Room} objects from the underlying data source using the provided database connection.
     *
     * <p>
     * The method relies on a valid {@link Connection} instance to execute the necessary SQL queries for fetching
     * room data. The returned list should contain all room records available in the system at the time of invocation.
     * </p>
     *
     * @param connection the {@code Connection} object used to interact with the database
     * @return a {@code List} of {@link Room} objects representing all available rooms
     */
    List<Room> getAllRooms(Connection connection);
}
