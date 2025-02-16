package com.lancaster.lancasterfx.api;
import com.lancaster.lancasterfx.model.Event;

import java.util.List;

public interface CalenderDataProvider {
    // Method to get all events
    List<Event> getAllEvents();
    // Method to get all available slots
    List<Event> getAvailableSlots();
    // Method to get an event by ID
    Event getEventByID(String eventId);
    // Method to reserve a slot
    boolean reserveSlot(String eventId, int days);
}