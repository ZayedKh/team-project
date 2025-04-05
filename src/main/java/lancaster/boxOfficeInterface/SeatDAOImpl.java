package lancaster.boxOfficeInterface;

import lancaster.model.Seat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class SeatDAOImpl implements SeatDAO {

    @Override
    public List<Seat> getSeatsByRoomId(Connection conn, int roomId) throws SQLException {
        String query = "SELECT * FROM Seats WHERE room_id = ? ORDER BY row_number, seat_number";
        return executeSeatQuery(conn, query, roomId);
    }

    @Override
    public List<Seat> getAccessibleSeats(Connection conn, int roomId) throws SQLException {

        String query = "SELECT * FROM Seats WHERE room_id = ? AND is_accessible = TRUE";
        return executeSeatQuery(conn, query, roomId);
    }

    @Override
    public List<Seat> getWheelchairSeats(Connection conn, int roomId) throws SQLException {
        String query = "SELECT * FROM Seats WHERE room_id = ? AND is_wheelchair_friendly = TRUE";
        return executeSeatQuery(conn, query, roomId);
    }

    private List<Seat> executeSeatQuery(Connection conn, String query, int roomId) throws SQLException {
        List<Seat> seats = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Seat seat = new Seat();
                seat.setSeatId(rs.getInt("seat_id"));
                seat.setRowNumber(rs.getInt("row_number"));
                seat.setSeatNumber(rs.getInt("seat_number"));
                seat.setAccessible(rs.getBoolean("is_accessible"));
                seat.setWheelchairFriendly(rs.getBoolean("is_wheelchair_friendly"));
                seats.add(seat);
            }
        }
        return seats;
    }
}