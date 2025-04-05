package lancaster.model;

import java.util.ArrayList;
import java.util.List;

public class BookingGroup {

    private final List<BookingDetails> bookings = new ArrayList<>();

    public void addBooking(BookingDetails booking) {
        bookings.add(booking);
    }

    public void submitAll() {
        for (BookingDetails booking : bookings) {
            System.out.println("Submitting booking: " + booking);
        }
        bookings.clear();
    }


    public List<BookingDetails> getBookings() {
        return bookings;
    }
}
