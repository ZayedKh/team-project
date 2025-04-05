package lancaster.boxOfficeInterface;

import lancaster.model.Seat;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

public class Test {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        BoxOfficeJDBC db = new BoxOfficeJDBC();

        List<Seat> wheelchairSeats = db.getWheelchairSeats(1);
        boolean hasAccessibleSeats = db.hasAccessibleSeating(1);
        System.out.println(hasAccessibleSeats);

        for (Seat seat : wheelchairSeats) {
            String seatInfo = seat.toString();
            System.out.println(seatInfo);
        }

        LocalTime startDate = db.getEventStartTime(1);
        System.out.println("Start date of event ID: 1: " + startDate);
        System.out.println("End date of event ID: 1: " + db.getEventEndTime(1));
        System.out.println("Duration of event ID 1: " + db.getEventDuration(1));
        System.out.println("Date of event ID 1: " + db.getEventDate(1));


    }
}
