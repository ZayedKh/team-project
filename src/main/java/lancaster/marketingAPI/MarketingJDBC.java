package lancaster.marketingAPI;

import lancaster.boxOfficeInterface.BoxOfficeJDBC;
import lancaster.model.Event;
import lancaster.model.Room;
import lancaster.model.Seat;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

/**
 * The {@code MarketingJDBC} class utilises the methods that need a connection to the database.
 * <p>
 * This class uses all of the methods that will require a {@code connection} to the database via JDBC
 * </p>
 *
 */
public class MarketingJDBC {
    private final Connection connection;
    private final RoomDAOImpl roomDAO;
    private final EventDAOImpl eventDAO;

    /**
     * Constructor for DBUtils that creates a connection between the user and the database
     * @throws SQLException             If a database access error occurs
     * @throws IOException              If an error occurs reading from input stream
     * @throws ClassNotFoundException   If the jdbc class cannot be found
     */
    public MarketingJDBC() throws ClassNotFoundException, SQLException, IOException {
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            //reads credentials from config file provided
            if (input != null) {
                throw new IOException("Unable to load config.properties file");
            }
            properties.load(input);
        }


        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        this.connection = DriverManager.getConnection(url, username, password);
        this.roomDAO = new RoomDAOImpl();
        this.eventDAO = new EventDAOImpl();
    }

    /**
     * Retrieves all room records from the underlying database.
     * <p>
     * This method executes a SQL query to fetch room details including room ID, name, type, seating configurations,
     * capacities, facilities, booking priority, and usage restrictions. Each record is mapped to a {@link Room} object.
     * The method then returns a list containing all the room objects retrieved from the database.
     * </p>
     *
     * @return a {@code List} of {@link Room} objects representing all available rooms
     * @throws RuntimeException if an {@link SQLException} occurs during the database access
     */
    public List<Room> getAllRooms() throws SQLException {
        return roomDAO.getAllRooms(connection);
    }

    /**
     * Retrieves all events with a status of "SCHEDULED" from the database.
     *
     * @return a {@code List} of {@link Event} objects representing all scheduled events
     * @throws SQLException if a database access error occurs
     */
    public List<Event> getAllScheduledEvents() throws SQLException {
        return eventDAO.getAllScheduledEvents(connection);
    }

    /**
     * Retrieves an event by its identifier.
     *
     * @param eventId the unique identifier of the event as a {@code String}
     * @return the {@link Event} object corresponding to the specified event identifier, or {@code null} if not found
     * @throws SQLException if a database access error occurs
     */
    public Event getEventById(String eventId) throws SQLException {
        return eventDAO.getEventById(connection, eventId);
    }

    /**
     * Retrieves upcoming events scheduled from the current time up to two days in the future.
     *
     * @return a {@code List} of {@link Event} objects representing upcoming events within the next two days
     * @throws SQLException if a database access error occurs
     */
    public List<Event> getUpcomingEvents() throws SQLException {
        return eventDAO.getUpcomingEvents(connection);
    }

    public static void main(String[] args) {
        try {
            MarketingJDBC marketingJDBC = new MarketingJDBC();
            System.out.println("MarketingJDBC initialized successfully.");

        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}