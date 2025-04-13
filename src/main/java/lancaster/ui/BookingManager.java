package lancaster.ui;

import lancaster.model.BookingDetails;
import lancaster.model.BookingGroup;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages booking data including creating bookings, handling multi-day bookings,
 * and checking for scheduling conflicts.
 * <p>
 * This class uses a {@link BookingGroup} to group bookings and supports adding single or multi-day bookings.
 * It also maintains a collection of selected dates and daily room bookings. Conflict detection methods
 * are provided to ensure overlapping bookings are identified.
 * </p>
 */
public class BookingManager {
    private BookingGroup bookingGroup = new BookingGroup();
    private List<LocalDate> selectedDates = new ArrayList<>();
    private Map<LocalDate, List<RoomBooking>> dailyBookings = new HashMap<>();

    /**
     * Represents a booking for a specific room at given start and end times.
     */
    public static class RoomBooking {
        String room;
        String startTime;
        String endTime;

        /**
         * Constructs a new {@code RoomBooking} with the specified room and time details.
         *
         * @param room      the room name.
         * @param startTime the booking start time.
         * @param endTime   the booking end time.
         */
        RoomBooking(String room, String startTime, String endTime) {
            this.room = room;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }

    /**
     * Creates a single booking for a specific date and room with the given time and event details.
     *
     * @param date      the date of the booking.
     * @param room      the room for the booking.
     * @param startTime the booking start time.
     * @param endTime   the booking end time.
     * @param eventName the name of the event.
     * @param clientName the name of the client booking the event.
     * @param roomType  the type of room being booked.
     */
    public void createSingleBooking(LocalDate date, String room, String startTime, String endTime, String eventName, String clientName, String roomType) {
        BookingDetails booking = new BookingDetails();
        booking.setDate(date);
        booking.setRoom(room);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setEventName(eventName);
        booking.setClientName(clientName);
        booking.setRoomType(roomType);
        bookingGroup.addBooking(booking);
    }

    /**
     * Creates multiple bookings across different days.
     * <p>
     * For each entry in the daily bookings map, a booking is created for the specified room and time,
     * associating them with the given client and event name. Existing bookings in the booking group are cleared before adding.
     * </p>
     *
     * @param clientName the name of the client booking the events.
     * @param eventName  the name of the event.
     */
    public void createMultiDayBookings(String clientName, String eventName) {
        bookingGroup.getBookings().clear();
        for (Map.Entry<LocalDate, List<RoomBooking>> entry : dailyBookings.entrySet()) {
            LocalDate date = entry.getKey();
            for (RoomBooking rb : entry.getValue()) {
                BookingDetails booking = new BookingDetails();
                booking.setDate(date);
                booking.setRoom(rb.room);
                booking.setStartTime(rb.startTime);
                booking.setEndTime(rb.endTime);
                booking.setEventName(eventName);
                booking.setClientName(clientName);
                bookingGroup.addBooking(booking);
            }
        }
    }

    /**
     * Checks for scheduling conflicts within the current set of bookings.
     * <p>
     * A conflict is detected if two or more bookings have the same date, room, start time, and end time.
     * </p>
     *
     * @return {@code true} if a conflict exists; {@code false} otherwise.
     */
    public boolean hasConflicts() {
        List<BookingDetails> bookings = bookingGroup.getBookings();
        for (int i = 0; i < bookings.size(); i++) {
            for (int j = i + 1; j < bookings.size(); j++) {
                BookingDetails b1 = bookings.get(i);
                BookingDetails b2 = bookings.get(j);
                if (b1.getDate().equals(b2.getDate()) &&
                        b1.getRoom().equals(b2.getRoom()) &&
                        b1.getStartTime().equals(b2.getStartTime()) &&
                        b1.getEndTime().equals(b2.getEndTime())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines whether a specific booking is conflicting with another booking.
     * <p>
     * The conflict is defined by matching date, room, start time, and end time. If more than one booking
     * exists with those details, the method returns {@code true}.
     * </p>
     *
     * @param booking the booking to check for conflicts.
     * @return {@code true} if the booking is conflicting with another; {@code false} otherwise.
     */
    public boolean isBookingConflicting(BookingDetails booking) {
        int count = 0;
        for (BookingDetails b : bookingGroup.getBookings()) {
            if (booking.getDate().equals(b.getDate()) &&
                    booking.getRoom().equals(b.getRoom()) &&
                    booking.getStartTime().equals(b.getStartTime()) &&
                    booking.getEndTime().equals(b.getEndTime())) {
                count++;
            }
        }
        return count > 1;
    }

    /**
     * Retrieves the current booking group containing all bookings.
     *
     * @return the {@link BookingGroup} instance.
     */
    public BookingGroup getBookingGroup() {
        return bookingGroup;
    }

    /**
     * Retrieves the list of selected dates associated with the bookings.
     *
     * @return a list of {@link LocalDate} objects representing selected dates.
     */
    public List<LocalDate> getSelectedDates() {
        return selectedDates;
    }

    /**
     * Retrieves the mapping of daily bookings for each date.
     *
     * @return a map where the key is a {@link LocalDate} and the value is a list of {@link RoomBooking} objects for that date.
     */
    public Map<LocalDate, List<RoomBooking>> getDailyBookings() {
        return dailyBookings;
    }
}
