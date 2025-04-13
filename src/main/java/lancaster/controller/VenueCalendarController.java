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
 * Controller class for managing and displaying the venue calendar.
 * <p>
 * This class is responsible for populating a calendar grid with booking information for various rooms.
 * It allows the user to navigate between dates, zoom using control-scroll gestures, and add new bookings.
 * The calendar grid is updated dynamically based on the selected date and the provided booking data.
 * </p>
 */
public class VenueCalendarController implements Initializable {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private GridPane calendarGrid;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button prevDayButton;

    @FXML
    private Button nextDayButton;

    @FXML
    private Label currentDateLabel;

    @FXML
    private Button addBookingButton;

    /**
     * List of room names available in the venue.
     */
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
    );

    /**
     * A map storing booking status data.
     * <p>
     * The key is the room name and the value is another map containing booking date/times and their venue statuses.
     * </p>
     */
    private Map<String, Map<LocalDateTime, VenueStatus>> bookings = new HashMap<>();

    private double scaleFactor = 1.0;
    private LocalDate currentDate = LocalDate.now();
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
    private SelectionPaneController selectionPaneController;

    /**
     * Sets the reference to the selection pane controller.
     *
     * @param selectionPaneController the controller responsible for managing the selection pane.
     */
    public void setSelectionPaneController(SelectionPaneController selectionPaneController) {
        this.selectionPaneController = selectionPaneController;
    }

    /**
     * Initializes the controller and configures the UI components.
     * <p>
     * This method sets the initial date value for the date picker, updates the current date label,
     * attaches event handlers for scrolling and date changes, and populates the calendar grid.
     * </p>
     *
     * @param location  the location used to resolve relative paths for the root object, or {@code null} if not known.
     * @param resources the resources used to localize the root object, or {@code null} if not localized.
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
     * Populates the calendar grid with time slots and room booking cells.
     * <p>
     * The grid is first cleared and its constraints are reset.
     * A time column and a header row with room names are created followed by time slot rows from 10:00 AM to midnight.
     * For each time slot and room, a cell is created reflecting the booking status.
     * </p>
     */
    private void populateCalendar() {
        calendarGrid.getChildren().clear();
        calendarGrid.getColumnConstraints().clear();
        calendarGrid.getRowConstraints().clear();

        // Set up column constraints for time column
        ColumnConstraints timeColumn = new ColumnConstraints();
        timeColumn.setMinWidth(100);
        timeColumn.setPrefWidth(100);
        calendarGrid.getColumnConstraints().add(timeColumn);

        // Set up column constraints for each room
        for (int i = 0; i < rooms.size(); i++) {
            ColumnConstraints roomColumn = new ColumnConstraints();
            roomColumn.setHgrow(Priority.ALWAYS);
            roomColumn.setFillWidth(true);
            roomColumn.setMinWidth(120);
            calendarGrid.getColumnConstraints().add(roomColumn);
        }

        // Add header cell for Time / Room
        VBox headerBox = new VBox();
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setStyle("-fx-background-color: #F5F5F5; -fx-padding: 10; -fx-border-color: #E0E0E0; -fx-border-width: 0 1 1 0;");
        Label headerLabel = new Label("Time / Room");
        headerLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        headerBox.getChildren().add(headerLabel);
        calendarGrid.add(headerBox, 0, 0);

        // Add headers for each room
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

        // Create time slots from 10:00 AM to midnight (00:00)
        int rowIndex = 1;
        for (int hour = 10; hour < 24; hour++) {
            LocalTime startTime = LocalTime.of(hour % 24, 0);
            LocalTime endTime = startTime.plusHours(1);
            // Create time label cell
            VBox timeBox = new VBox();
            timeBox.setAlignment(Pos.CENTER);
            timeBox.setStyle("-fx-background-color: #F5F5F5; -fx-padding: 10; -fx-border-color: #E0E0E0; -fx-border-width: 0 1 1 0;");
            String timeText = startTime.format(timeFormatter) + " - " + endTime.format(timeFormatter);
            Label timeLabel = new Label(timeText);
            timeBox.getChildren().add(timeLabel);
            calendarGrid.add(timeBox, 0, rowIndex);

            // Create cells for each room at the current time slot
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
     * Creates and returns a booking cell for a specific room and time.
     * <p>
     * This cell visually represents the booking status using color coding and includes a tooltip with detailed information.
     * </p>
     *
     * @param room     the room name.
     * @param dateTime the specific date and time slot for the booking.
     * @param status   the current booking status for the slot.
     * @return a {@link StackPane} representing the booking cell.
     */
    private StackPane createBookingCell(String room, LocalDateTime dateTime, VenueStatus status) {
        StackPane cellPane = new StackPane();
        cellPane.setPadding(new Insets(10));
        cellPane.setUserData(new BookingData(room, dateTime, status));

        // Apply style based on the booking status
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

        // Apply text color based on status
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

        // Set tooltip with detailed booking information
        Tooltip tooltip = new Tooltip(room + "\n" +
                dateTime.format(DateTimeFormatter.ofPattern("HH:mm")) + " - " +
                dateTime.plusHours(1).format(DateTimeFormatter.ofPattern("HH:mm")) + "\n" +
                status.getDescription());
        Tooltip.install(cellPane, tooltip);

        return cellPane;
    }

    /**
     * Updates the calendar grid with a list of booking details.
     * <p>
     * For each booking detail, the booking status of the corresponding room and time slot is set to UNAVAILABLE,
     * and the calendar is refreshed to reflect these changes.
     * </p>
     *
     * @param bookingList a list of {@link BookingDetails} containing booking information.
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
     * Handles the action to navigate to the previous day.
     * <p>
     * The current date is decreased by one day, the date picker is updated,
     * and the calendar grid is repopulated.
     * </p>
     */
    @FXML
    private void handlePreviousDay() {
        currentDate = currentDate.minusDays(1);
        datePicker.setValue(currentDate);
        updateCurrentDateLabel();
        populateCalendar();
    }

    /**
     * Handles the action to navigate to the next day.
     * <p>
     * The current date is increased by one day, the date picker is updated,
     * and the calendar grid is repopulated.
     * </p>
     */
    @FXML
    private void handleNextDay() {
        currentDate = currentDate.plusDays(1);
        datePicker.setValue(currentDate);
        updateCurrentDateLabel();
        populateCalendar();
    }

    /**
     * Handles the action to add a new booking.
     * <p>
     * If the selection pane controller is set, it triggers the display of the booking pane.
     * Otherwise, it logs that the controller reference is missing.
     * </p>
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
     * Updates the current date label with a formatted date string.
     * <p>
     * The date is formatted using a predefined date formatter.
     * </p>
     */
    private void updateCurrentDateLabel() {
        currentDateLabel.setText(currentDate.format(dateFormatter));
    }

    /**
     * Handles zooming of the calendar when the user scrolls while holding the Control key.
     * <p>
     * Adjusts the scale factor of the main border pane and updates the scroll pane settings accordingly.
     * </p>
     *
     * @param event the {@link ScrollEvent} triggered by the user.
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
     * Retrieves the booking status for a given room and time slot.
     * <p>
     * Checks if there is a booked status for the room at the specific date and time,
     * otherwise returns {@code AVAILABLE} as the default status.
     * </p>
     *
     * @param room     the room name.
     * @param dateTime the date and time slot to check for a booking.
     * @return the {@link VenueStatus} associated with the given room and time slot.
     */
    private VenueStatus getBookingStatus(String room, LocalDateTime dateTime) {
        if (bookings.containsKey(room) && bookings.get(room).containsKey(dateTime)) {
            return bookings.get(room).get(dateTime);
        }
        return VenueStatus.AVAILABLE; // Default status
    }

    /**
     * A helper class to encapsulate booking data for each calendar cell.
     * <p>
     * This data structure stores the room name, the date/time of the booking, and its current status.
     * It is set as the user data for each booking cell in the calendar grid.
     * </p>
     */
    private static class BookingData {
        String room;
        LocalDateTime dateTime;
        VenueStatus status;

        /**
         * Constructs a {@code BookingData} instance with specified room, time, and status.
         *
         * @param room     the room name.
         * @param dateTime the date and time associated with the booking.
         * @param status   the booking status.
         */
        BookingData(String room, LocalDateTime dateTime, VenueStatus status) {
            this.room = room;
            this.dateTime = dateTime;
            this.status = status;
        }
    }
}
