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

public class MarketingJDBC {
    private final Connection connection;
    private final RoomDAOImpl roomDAO;
    private final EventDAOImpl eventDAO;

    public MarketingJDBC() throws ClassNotFoundException, SQLException, IOException {
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
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

    public List<Room> getAllRooms() throws SQLException {
        return roomDAO.getAllRooms(connection);
    }

    public List<Event> getAllScheduledEvents() throws SQLException {
        return eventDAO.getAllScheduledEvents(connection);
    }

    public Event getEventById(String eventId) throws SQLException {
        return eventDAO.getEventById(connection, eventId);
    }

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