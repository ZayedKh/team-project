package lancaster.boxOfficeInterface;

import lancaster.model.Seat;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

public class Test {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        BoxOfficeJDBC db = new BoxOfficeJDBC();

        System.out.println("TESTING SEATING CONFIG");
        List<Seat> wheelchairSeats = db.getWheelchairSeats(1);
        boolean hasAccessibleSeats = db.hasAccessibleSeating(1);
        System.out.println(hasAccessibleSeats);

        for (Seat seat : wheelchairSeats) {
            String seatInfo = seat.toString();
            System.out.println(seatInfo);
        }
        System.out.println();

        System.out.println("TESTING  BOOKING CONFIG");
        LocalTime startDate = db.getEventStartTime(1);
        System.out.println("Start date of event ID: 1: " + startDate);
        System.out.println("End date of event ID: 1: " + db.getEventEndTime(1));
        System.out.println("Duration of event ID 1: " + db.getEventDuration(1));
        System.out.println("Date of event ID 1: " + db.getEventDate(1));
        System.out.println();

        System.out.println("TESTING ROOM CONFIG");
        System.out.println(db.getRoomCapacity(10));
        System.out.println(db.getLastBookingTime(10));
        System.out.println();

    }
}
