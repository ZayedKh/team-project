package lancaster.ui;

import lancaster.model.BookingDetails;
import lancaster.model.BookingGroup;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingManager {
    private BookingGroup bookingGroup = new BookingGroup();
    private List<LocalDate> selectedDates = new ArrayList<>();
    private Map<LocalDate, List<RoomBooking>> dailyBookings = new HashMap<>();

    public static class RoomBooking {
        String room;
        String startTime;
        String endTime;

        RoomBooking(String room, String startTime, String endTime) {
            this.room = room;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }

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
    public BookingGroup getBookingGroup() {
        return bookingGroup;
    }

    public List<LocalDate> getSelectedDates() {
        return selectedDates;
    }

    public Map<LocalDate, List<RoomBooking>> getDailyBookings() {
        return dailyBookings;
    }
}