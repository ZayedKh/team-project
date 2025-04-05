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

    public void createSingleBooking(LocalDate date, String room, String startTime, String endTime, String eventName, String clientName) {
        BookingDetails booking = new BookingDetails();
        booking.setDate(date);
        booking.setRoom(room);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setEventName(eventName);
        booking.setClientName(clientName);
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