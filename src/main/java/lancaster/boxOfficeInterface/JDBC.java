package lancaster.boxOfficeInterface;

import lancaster.model.Seat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class JDBC {
    private final Connection connection;
    private SeatDAOImpl seatingConfigDAO;


    public JDBC() throws SQLException, ClassNotFoundException {
        String url = "jdbc:mysql://sst-stuproj00:3306/in2033t44";
        String username = "in2033t44_a";
        String password = "wcYtgG2jphQ";

        Class.forName("com.mysql.cj.jdbc.Driver");

        this.connection = DriverManager.getConnection(url, username, password);
        this.seatingConfigDAO = new SeatDAOImpl();
    }

    public boolean hasAccessableSeating(int roomId) throws SQLException, ClassNotFoundException {
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
}
