package lancaster.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a group of booking details to be processed together.
 * <p>
 * This class manages a collection of {@link BookingDetails} objects that can be added individually
 * and then submitted all at once.
 * </p>
 */
public class BookingGroup {

    private final List<BookingDetails> bookings = new ArrayList<>();

    /**
     * Adds a booking to the group.
     * <p>
     * The provided {@link BookingDetails} object is appended to the internal collection of bookings.
     * </p>
     *
     * @param booking the booking details to add.
     */
    public void addBooking(BookingDetails booking) {
        bookings.add(booking);
    }

    /**
     * Submits all bookings in the group.
     * <p>
     * This method iterates over each {@link BookingDetails} in the group for submission.
     * </p>
     */
    public void submitAll() {
        for (BookingDetails booking : bookings) {
            System.out.println("Submitting booking: " + booking);
        }
        bookings.clear();
    }

    /**
     * Retrieves the list of booking details in the group.
     * <p>
     * This method returns the internal list of {@link BookingDetails}. Modifications to the list
     * will affect the group's internal state.
     * </p>
     *
     * @return the list of bookings.
     */
    public List<BookingDetails> getBookings() {
        return bookings;
    }
}
