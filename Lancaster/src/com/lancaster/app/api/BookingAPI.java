package com.lancaster.app.api;
import com.lancaster.app.model.*;

// API for managing booking functions, this allows marketing to book large groups & meetings
// Can be used by marketing and box office
public interface BookingAPI {
   // Method to create an event, returns an com.lancaster.app.api.model.Event object
   Event createEvent(String eventId, int days, String status, String eventType, double cost);
   // Method to update an events details
   void updateEvent(int bookingId, String newDetails);
   // function to cancel/delete a booking.
   void cancelEvent(int bookingId);
}
