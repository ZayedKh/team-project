# Deliverable Overview
This folder contains the required interfaces/classes/data access objects (DAO).

The project structure is as such:

📦 src  
┣ 📂 classes          → Data models (DailySheet, Event)  
┣ 📂 DAO          → Data access objects to fetch data from database   
┣ 📂 interfaces     → interfaces  

To access data, please use the data access objects and their provided methods.

Concrete classes of abstract classes have been provided as dummy classes, please replace them with actual implementations.

Example usage of the DAOs:

```java

import DAO.CalendarDAO;

import java.sql.SQLException;

public static void main(String[] args) {
    // Database connection setup
    String url = "database_url";
    String user = "your_username";
    String password = "your_password";

    try (Connection connection = DriverManager.getConnection(url, user, password)) {
        CalendarDAO calendarDAO = new CalendarDAO(connection);

        // Example: Retrieve event by month
        ArrayList<Event> monthsInApril = calendarDAO.getEventByMonth(connection, 3); // 3 - April

        // Example Retrieve all events in chronological order
        ArrayList<Event> recentEvents = calendarDAO.getMostRecentEvents(connection);
        
        // Print recent events
        for(Event event : recentEvents){
            event ? System.out.println("Retrieved Event: " + event) : System.out.println("Event not found");
        }
    } catch (SQLException e){
        e.printStackTrace();
    }
}

```
