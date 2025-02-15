package com.lancaster.app.api;

import java.util.List;
public interface CalenderDataProvider {
    List<Event> getAllEvents();
    List<Event> getAvailableSlots();
    Event getEventByID(String eventId);
    boolean reserveSlot(String eventId, int days);
}
