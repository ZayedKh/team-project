package lancaster.boxOfficeInterface;

import lancaster.model.Seat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class BoxOfficeJDBC {
    private final Connection connection;
    private final SeatDAOImpl seatingConfigDAO;
    private final EventDAOImpl eventDAO;


    public BoxOfficeJDBC() throws SQLException, ClassNotFoundException {
        String url = "jdbc:mysql://sst-stuproj00:3306/in2033t44";
        String username = "in2033t44_a";
        String password = "wcYtgG2jphQ";

        Class.forName("com.mysql.cj.jdbc.Driver");

        this.connection = DriverManager.getConnection(url, username, password);
        this.seatingConfigDAO = new SeatDAOImpl();

        this.eventDAO = new EventDAOImpl();
    }


    public boolean hasAccessibleSeating(int roomId) throws SQLException {
        return seatingConfigDAO.hasAccessableSeating(connection, roomId);
    }

    public List<Seat> getSeatsByRoomId(int roomId) throws SQLException {
        return seatingConfigDAO.getSeatsByRoomId(connection, roomId);
    }

    public List<Seat> getAccessibleSeats(int roomId) throws SQLException {
        return seatingConfigDAO.getAccessibleSeats(connection, roomId);
    }

    public List<Seat> getWheelchairSeats(int roomId) throws SQLException {
        return seatingConfigDAO.getWheelchairSeats(connection, roomId);
    }

    public LocalTime getEventStartTime(int eventID) throws SQLException {
        return eventDAO.getStartTime(connection, eventID);
    }

    public LocalTime getEventEndTime(int eventID) throws SQLException {
        return eventDAO.getEndTime(connection, eventID);
    }

    public LocalDate getEventDate(int eventID) throws SQLException {
        return eventDAO.getEventStartDate(connection, eventID);
    }

    public int getEventDuration(int eventID) throws SQLException {
        return eventDAO.getDuration(connection, eventID);
    }

    public int getEventRoomID(int eventID) throws SQLException {
        return eventDAO.getRoomID(connection, eventID);
    }

    public ResultSet getEventData(int eventID) throws SQLException {
        return eventDAO.getData(connection, eventID);
    }
}
