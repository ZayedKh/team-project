// API for managing booking functions, this allows marketing to book large groups & meetings
// Can be used by marketing and box office
public interface BookingAPI {
   Event createEvent(String eventId, int days, String status, String eventType, double cost);
   void updateBooking(int bookingId, String newDetails);
   void cancelBooking(int bookingId);
}
