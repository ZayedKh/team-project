package lancaster.controller;

import lancaster.model.BookingDetails;
import lancaster.model.VenueStatus;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Controller class for managing the venue calendar and booking visualization in a JavaFX application.
 */
public class VenueCalendarController implements Initializable {

    @FXML private ScrollPane scrollPane;        // Scroll pane containing the calendar
    @FXML private BorderPane mainBorderPane;    // Main container for the calendar layout
    @FXML private GridPane calendarGrid;        // Grid displaying the calendar
    @FXML private DatePicker datePicker;        // Date picker for selecting the current date
    @FXML private Button prevDayButton;         // Button to navigate to the previous day
    @FXML private Button nextDayButton;         // Button to navigate to the next day
    @FXML private Label currentDateLabel;       // Label displaying the current date
    @FXML private Button addBookingButton;      // Button to initiate adding a new booking

    private final List<String> rooms = Arrays.asList(
            "The Green Room",
            "Brontë Boardroom",
            "Dickens Den",
            "Poe Parlor",
            "Globe Room",
            "Chekhov Chamber",
            "Main Hall",
            "Small Hall",
            "Rehearsal Space"
    );  // List of available rooms

    // Mock data for bookings
    private Map<String, Map<LocalDateTime, VenueStatus>> bookings = new HashMap<>(); // Tracks booking status by room and time
    private double scaleFactor = 1.0;           // Zoom scale factor for calendar display
    private LocalDate currentDate = LocalDate.now(); // Currently displayed date
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm"); // Formatter for time display
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"); // Formatter for date display
    private SelectionPaneController selectionPaneController; // Reference to the selection pane controller

    /**
     * Sets the SelectionPaneController instance for navigation purposes.
     * @param selectionPaneController The controller to set
     */
    public void setSelectionPaneController(SelectionPaneController selectionPaneController) {
        this.selectionPaneController = selectionPaneController;
    }

    /**
     * Initializes the controller after its root element has been processed.
     * @param location The location used to resolve relative paths for the root object
     * @param resources The resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        datePicker.setValue(currentDate);
        updateCurrentDateLabel();
        mainBorderPane.setOnScroll(this::handleScroll);
        datePicker.setOnAction(event -> {
            currentDate = datePicker.getValue();
            updateCurrentDateLabel();
            populateCalendar();
        });
        populateCalendar();
    }

    /**
     * Populates the calendar grid with time slots and room availability.
     */
    private void populateCalendar() {
        calendarGrid.getChildren().clear();
        calendarGrid.getColumnConstraints().clear();
        calendarGrid.getRowConstraints().clear();

        // Set up column constraints
        ColumnConstraints timeColumn = new ColumnConstraints();
        timeColumn.setMinWidth(100);
        timeColumn.setPrefWidth(100);
        calendarGrid.getColumnConstraints().add(timeColumn);

        for (int i = 0; i < rooms.size(); i++) {
            ColumnConstraints roomColumn = new ColumnConstraints();
            roomColumn.setHgrow(Priority.ALWAYS);
            roomColumn.setFillWidth(true);
            roomColumn.setMinWidth(120);
            calendarGrid.getColumnConstraints().add(roomColumn);
        }

        // Add time/room header
        VBox headerBox = new VBox();
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setStyle("-fx-background-color: #F5F5F5; -fx-padding: 10; -fx-border-color: #E0E0E0; -fx-border-width: 0 1 1 0;");
        Label headerLabel = new Label("Time / Room");
        headerLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        headerBox.getChildren().add(headerLabel);
        calendarGrid.add(headerBox, 0, 0);

        // Add room headers
        for (int i = 0; i < rooms.size(); i++) {
            VBox roomHeader = new VBox();
            roomHeader.setAlignment(Pos.CENTER);
            roomHeader.setStyle("-fx-background-color: #F5F5F5; -fx-padding: 10; -fx-border-color: #E0E0E0; -fx-border-width: 0 1 1 0;");
            Label roomLabel = new Label(rooms.get(i));
            roomLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
            roomLabel.setWrapText(true);
            roomHeader.getChildren().add(roomLabel);
            calendarGrid.add(roomHeader, i + 1, 0);
        }

        // Add time slots from 10:00 AM to midnight (00:00)
        int rowIndex = 1;
        for (int hour = 10; hour < 24; hour++) {
            LocalTime startTime = LocalTime.of(hour % 24, 0);
            LocalTime endTime = startTime.plusHours(1);
            // Time label
            VBox timeBox = new VBox();
            timeBox.setAlignment(Pos.CENTER);
            timeBox.setStyle("-fx-background-color: #F5F5F5; -fx-padding: 10; -fx-border-color: #E0E0E0; -fx-border-width: 0 1 1 0;");
            String timeText = startTime.format(timeFormatter) + " - " + endTime.format(timeFormatter);
            Label timeLabel = new Label(timeText);
            timeBox.getChildren().add(timeLabel);
            calendarGrid.add(timeBox, 0, rowIndex);

            // Room cells for this time slot
            for (int i = 0; i < rooms.size(); i++) {
                String room = rooms.get(i);
                LocalDateTime dateTime = LocalDateTime.of(currentDate, startTime);
                VenueStatus status = getBookingStatus(room, dateTime);
                StackPane cellPane = createBookingCell(room, dateTime, status);
                calendarGrid.add(cellPane, i + 1, rowIndex);
            }
            rowIndex++;
        }
    }

    /**
     * Creates a cell representing a booking status for a specific room and time.
     * @param room The room name
     * @param dateTime The date and time of the booking
     * @param status The status of the venue at this time
     * @return StackPane representing the booking cell
     */
    private StackPane createBookingCell(String room, LocalDateTime dateTime, VenueStatus status) {
        StackPane cellPane = new StackPane();
        cellPane.setPadding(new Insets(10));
        cellPane.setUserData(new BookingData(room, dateTime, status));

        // Set style based on status
        switch (status) {
            case AVAILABLE:
                cellPane.setStyle("-fx-background-color: #E8F5E9; -fx-border-color: #E0E0E0; -fx-border-width: 0 1 1 0;");
                break;
            case RESERVED:
                cellPane.setStyle("-fx-background-color: #FFF8E1; -fx-border-color: #E0E0E0; -fx-border-width: 0 1 1 0;");
                break;
            case UNAVAILABLE:
                cellPane.setStyle("-fx-background-color: #FFEBEE; -fx-border-color: #E0E0E0; -fx-border-width: 0 1 1 0;");
                break;
        }

        VBox content = new VBox(5);
        content.setAlignment(Pos.CENTER);

        Label statusLabel = new Label(status.name());
        statusLabel.setWrapText(true);

        // Set text color based on status
        switch (status) {
            case AVAILABLE:
                statusLabel.setTextFill(Color.valueOf("#4CAF50"));
                break;
            case RESERVED:
                statusLabel.setTextFill(Color.valueOf("#FFA000"));
                break;
            case UNAVAILABLE:
                statusLabel.setTextFill(Color.valueOf("#D32F2F"));
                break;
        }

        content.getChildren().add(statusLabel);
        cellPane.getChildren().add(content);

        // Add tooltip with detailed information
        Tooltip tooltip = new Tooltip(room + "\n" +
                dateTime.format(DateTimeFormatter.ofPattern("HH:mm")) + " - " +
                dateTime.plusHours(1).format(DateTimeFormatter.ofPattern("HH:mm")) + "\n" +
                status.getDescription());
        Tooltip.install(cellPane, tooltip);

        return cellPane;
    }

    /**
     * Updates the calendar with a list of booking details.
     * @param bookingList List of bookings to update the calendar with
     */
    public void updateCalendarWithBookings(List<BookingDetails> bookingList) {
        for (BookingDetails booking : bookingList) {
            LocalTime startTime = LocalTime.parse(booking.getStartTime());
            LocalDateTime dateTime = LocalDateTime.of(booking.getDate(), startTime);
            if (bookings.containsKey(booking.getRoom())) {
                bookings.get(booking.getRoom()).put(dateTime, VenueStatus.UNAVAILABLE);
            } else {
                Map<LocalDateTime, VenueStatus> roomBookings = new HashMap<>();
                roomBookings.put(dateTime, VenueStatus.UNAVAILABLE);
                bookings.put(booking.getRoom(), roomBookings);
            }
        }
        populateCalendar();
    }

    /**
     * Handles navigation to the previous day.
     */
    @FXML
    private void handlePreviousDay() {
        currentDate = currentDate.minusDays(1);
        datePicker.setValue(currentDate);
        updateCurrentDateLabel();
        populateCalendar();
    }

    /**
     * Handles navigation to the next day.
     */
    @FXML
    private void handleNextDay() {
        currentDate = currentDate.plusDays(1);
        datePicker.setValue(currentDate);
        updateCurrentDateLabel();
        populateCalendar();
    }

    /**
     * Handles the action to add a new booking by navigating to the booking pane.
     */
    @FXML
    private void handleAddBooking() {
        if (selectionPaneController != null) {
            selectionPaneController.showBookingPane();
        } else {
            System.out.println("SelectionPaneController is not set.");
        }
    }

    /**
     * Updates the current date label with the formatted date.
     */
    private void updateCurrentDateLabel() {
        currentDateLabel.setText(currentDate.format(dateFormatter));
    }

    /**
     * Handles scroll events for zooming the calendar display.
     * @param event The scroll event
     */
    private void handleScroll(ScrollEvent event) {
        if (event.isControlDown()) {
            double deltaY = event.getDeltaY();
            if (deltaY > 0) {
                scaleFactor += 0.1;
            } else if (deltaY < 0) {
                scaleFactor -= 0.1;
            }
            scaleFactor = Math.max(0.5, Math.min(2.0, scaleFactor));
            mainBorderPane.setScaleX(scaleFactor);
            mainBorderPane.setScaleY(scaleFactor);
            scrollPane.setFitToWidth(scaleFactor >= 1.0);
            scrollPane.setFitToHeight(false);
            event.consume();
        }
    }

    /**
     * Retrieves the booking status for a specific room and time.
     * @param room The room name
     * @param dateTime The date and time to check
     * @return The venue status for the given room and time
     */
    private VenueStatus getBookingStatus(String room, LocalDateTime dateTime) {
        if (bookings.containsKey(room) && bookings.get(room).containsKey(dateTime)) {
            return bookings.get(room).get(dateTime);
        }
        return VenueStatus.AVAILABLE; // Default status
    }

    /**
     * Helper class to store booking data in each calendar cell.
     */
    private static class BookingData {
        String room;
        LocalDateTime dateTime;
        VenueStatus status;

        BookingData(String room, LocalDateTime dateTime, VenueStatus status) {
            this.room = room;
            this.dateTime = dateTime;
            this.status = status;
        }
    }
}