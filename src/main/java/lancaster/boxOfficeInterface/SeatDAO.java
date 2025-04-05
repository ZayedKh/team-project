package lancaster.boxOfficeInterface;

import lancaster.model.Seat;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface SeatDAO {
    List<Seat> getSeatsByRoomId(Connection conn, int roomId) throws SQLException;

    List<Seat> getAccessibleSeats(Connection conn, int roomId) throws SQLException;

    List<Seat> getWheelchairSeats(Connection conn, int roomId) throws SQLException;
}
