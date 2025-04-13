package lancaster.marketingAPI;

import lancaster.model.Event;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The {@code EventDAO} interface defines methods to access event data from a data source.
 * <p>
 * This interface abstracts database operations related to shows and events, allowing for flexibility in the underlying
 * implementation. It supports retrieving all scheduled events, finding a specific event by its identifier, and fetching
 * upcoming events within a specified time frame.
 * </p>
 *
 * This interface is part of the marketing API and is used for querying event records.
 */
public interface EventDAO {

    /**
     * Retrieves a comprehensive list of all events that are currently scheduled.
     * <p>
     * The method executes the required SQL queries using the provided {@code Connection} to fetch event details.
     * Each event is represented as an {@link Event} object. This method is useful for obtaining an overview of all the
     * events in the schedule.
     * </p>
     *
     * @param connection the {@code Connection} object used to execute database operations
     * @return a list of {@link Event} objects representing all scheduled events
     * @throws SQLException if a database access error occurs during the operation
     */
    List<Event> getAllScheduledEvents(Connection connection) throws SQLException;

    /**
     * Retrieves the details of a specific event identified by its unique identifier.
     * <p>
     * This method uses the provided {@code Connection} to execute a parameterized SQL query. If a matching event is found,
     * it is mapped to an {@link Event} object; otherwise, the method returns null.
     * </p>
     *
     * @param connection the {@code Connection} object used to execute the query
     * @param eventId    the unique identifier of the event as a {@code String}
     * @return an {@link Event} object representing the event details, or {@code null} if no matching event is found
     * @throws SQLException if a database access error occurs during the operation
     */
    public Event getEventById(Connection connection, String eventId) throws SQLException;

    /**
     * Retrieves a list of events that are scheduled to occur within the next 48 hours from the current time.
     * <p>
     * Using the provided {@code Connection}, this method executes a query to find events where the event date and time
     * fall within the current moment and two days ahead. The resulting events are mapped to {@link Event} objects.
     * </p>
     *
     * @param connection the {@code Connection} object used to execute database queries
     * @return a list of {@link Event} objects representing the upcoming events within the next 48 hours
     * @throws SQLException if a database access error occurs during the operation
     */
    public List<Event> getUpcomingEvents(Connection connection) throws SQLException;
}
