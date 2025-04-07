package lancaster.marketingAPI;

import lancaster.model.Room;

import java.sql.Connection;
import java.util.List;

public interface RoomDAO {
    List<Room> getAllRooms(Connection connection);
}
