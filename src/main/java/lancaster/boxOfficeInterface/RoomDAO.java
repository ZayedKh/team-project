package lancaster.boxOfficeInterface;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

public interface RoomDAO {
    int getRoomCapacity(Connection connection, int roomID) throws SQLException;
    Timestamp getLastBookingTime(Connection connection, int roomId) throws SQLException;
}
