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
        String url = "jdbc:mysql://localhost:3306/";
        String username = "in2033t44_a";
        String password = "wcYtgG2jphQ";

        Class.forName("com.mysql.cj.jdbc.Driver");

        this.connection = DriverManager.getConnection(url, username, password);
        this.seatingConfigDAO = new SeatDAOImpl();
    }

    public List<Seat> getSeatsByRoomId(Connection conn, int roomId) throws SQLException {
        return seatingConfigDAO.getSeatsByRoomId(conn, roomId);
    }

    public List<Seat> getAccessibleSeats(Connection conn, int roomId) throws SQLException {
        return seatingConfigDAO.getAccessibleSeats(conn, roomId);
    }

    public List<Seat> getWheelchairSeats(Connection conn, int roomId) throws SQLException {
        return seatingConfigDAO.getWheelchairSeats(conn, roomId);
    }
}
