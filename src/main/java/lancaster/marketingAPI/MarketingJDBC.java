package lancaster.marketingAPI;

import lancaster.model.Event;
import lancaster.model.Room;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class MarketingJDBC {
    private final Connection connection;
    private final RoomDAOImpl roomDAO;
    private final EventDAOImpl eventDAO;

    public MarketingJDBC() throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://sst-stuproj00:3306/in2033t44";
        String username = "in2033t44_a";
        String password = "wcYtgG2jphQ";

        Class.forName("com.mysql.cj.jdbc.Driver");

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
}