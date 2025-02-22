# Marketing Deliverable Overview

This folder contains the required interfaces/classes/data access objects (DAO).

The project structure is as such:

📦 src  
┣ 📂 classes → Data models (Room, Event etc.)  
┣ 📂 DAO → Data access objects to fetch data   
┣ 📂 interfaces → interfaces  
┣ 📂 implementations → implementations of interfaces  
┣ 📂 enums → enums to represent statuses etc.

To access data, please use the data access objects and their provided methods.

Concrete classes of abstract classes have been provided as dummy classes, please replace them with actual implementations.

Example usage of the DAOs:
```java
public static void main(String[] args) {
        // Database connection setup
        String url = "database_url";
        String user = "your_username";
        String password = "your_password";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            EventDAO eventDAO = new EventDAO(connection);

            // Example: Retrieve all events
            List<Event> events = eventDAO.getAllEvents();
            for (Event event : events) {
                System.out.println(event);
            }

            // Example: Retrieve a single event by ID
            Event event = eventDAO.getEventById("EVT123");
            if (event != null) {
                System.out.println("Retrieved Event: " + event);
            } else {
                System.out.println("Event not found.");
            }

            // Example: Add a new event
            Event newEvent = new YourConcreteEventClass(
                "EVT124", 
                "Tech Conference", 
                LocalDateTime.of(2025, 5, 20, 10, 0), 
                "Technology", 
                EventStatus.SCHEDULED, 
                LocalDateTime.now()
            );
            eventDAO.addEvent(newEvent);
            System.out.println("Event added successfully!");

            // Example: Update an event
            newEvent.setEventName("Updated Tech Conference");
            eventDAO.updateEvent(newEvent);
            System.out.println("Event updated successfully!");

            // Example: Delete an event
            eventDAO.deleteEvent("EVT124");
            System.out.println("Event deleted successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
```