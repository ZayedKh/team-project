package lancaster.marketingAPI;

import lancaster.marketingAPI.MarketingJDBC;
import lancaster.model.Booking;
import lancaster.model.Room;
import lancaster.model.Seat;
import lancaster.utils.DBUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MarketingTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {

        MarketingJDBC marketingJDBC = new MarketingJDBC();

        List<Room> rooms = marketingJDBC.getAllRooms();

        for (Room room : rooms) {
            System.out.println(room);
        }
    }
}