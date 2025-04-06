package lancaster.marketingAPI;

import lancaster.model.Event;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface EventDAO {

    /**
     * Retrieves a comprehensive list of all scheduled shows and events.
     *
     * @return A list of Event objects representing all scheduled events.
     */
    List<Event> getAllScheduledEvents(Connection connection) throws SQLException;

    /**
     * Retrieves the details of a specific event by its unique identifier.
     *
     * @param eventId The unique identifier of the event.
     * @return An Event object representing the event details, or null if not found.
     */
    public Event getEventById(Connection connection, String eventId) throws SQLException;

    /**
     * Retrieves a list of events that are scheduled to occur within the next 48 hours.
     *
     * @return A list of Event objects representing the upcoming events.
     */
    public List<Event> getUpcomingEvents(Connection connection) throws SQLException;
}
