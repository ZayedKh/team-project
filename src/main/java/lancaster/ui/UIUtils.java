package lancaster.ui;

import lancaster.model.BookingDetails;
import lancaster.model.BookingGroup;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * The UIUtils class provides various utility methods for constructing common UI components
 * used throughout the application.
 * <p>
 * These include methods for creating headers, room cells,
 * booking cells, text fields, time dropdowns, date chips, calendar containers, and showing alerts.
 * </p>
 */
public class UIUtils {

    /**
     * Creates a header bar with a back button, title, and (optionally) a pending bookings count.
     *
     * @param title      the title to display in the header
     * @param backAction an action to execute when the back button is clicked
     * @param bookingGroup the booking group whose pending bookings count is displayed if not empty
     * @return an HBox containing the constructed header
     */
    public static HBox createHeader(String title, Consumer<javafx.event.ActionEvent> backAction, BookingGroup bookingGroup) {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15));

        Button backButton = new Button("Back");
        backButton.setPrefSize(100, 30);
        backButton.setStyle("-fx-font-size: 14px;");
        backButton.setOnAction(backAction::accept);

        Label titleLabel = new Label(title);
        StackPane titleWrapper = new StackPane();
        titleWrapper.setStyle("-fx-background-color: #2ECC40; -fx-padding: 10px 25px; -fx-background-radius: 5px;");
        titleWrapper.setPadding(new Insets(8));
        titleWrapper.getChildren().add(titleLabel);
        titleLabel.setStyle("-fx-font-size: 20px;");

        header.getChildren().addAll(backButton, titleWrapper);
        if (!bookingGroup.getBookings().isEmpty()) {
            Label pendingLabel = new Label("Pending Bookings: " + bookingGroup.getBookings().size());
            pendingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #2ECC40;");
            header.getChildren().add(pendingLabel);
        }
        return header;
    }

    /**
     * Creates a room cell UI component representing a single room.
     * <p>
     * The room cell includes the room name and changes its style on mouse hover and click.
     * </p>
     *
     * @param room    the name of the room to be displayed
     * @param onClick a mouse event consumer to handle click actions on the room cell
     * @return an HBox representing the room cell
     */
    public static HBox createRoomCell(String room, Consumer<MouseEvent> onClick) { // Updated to javafx.scene.input.MouseEvent
        HBox roomCell = new HBox();
        roomCell.setAlignment(Pos.CENTER_LEFT);
        roomCell.setPrefHeight(60);
        roomCell.setPrefWidth(500);
        roomCell.setStyle("-fx-background-color: #e0ffe4; -fx-border-color: #122023; -fx-border-width: 1px;"
                + " -fx-padding: 10px 20px; -fx-background-radius: 5px;");

        Label roomLabel = new Label(room);
        roomLabel.setStyle("-fx-font-size: 18px;");
        roomCell.getChildren().add(roomLabel);

        roomCell.setOnMouseEntered(e -> roomCell.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #122023; -fx-border-width: 1px;"
                + " -fx-padding: 10px 20px; -fx-background-radius: 5px;"));
        roomCell.setOnMouseExited(e -> roomCell.setStyle("-fx-background-color: #e0ffe4; -fx-border-color: #122023; -fx-border-width: 1px;"
                + " -fx-padding: 10px 20px; -fx-background-radius: 5px;"));
        roomCell.setOnMouseClicked(onClick::accept);

        return roomCell;
    }

    /**
     * Creates a booking cell UI component for a given booking.
     * <p>
     * This method displays booking details and includes a remove button that, when clicked,
     * removes the booking from the booking group and refreshes the view.
     * </p>
     *
     * @param booking      the booking details to display
     * @param index        the index of the booking in the booking group
     * @param bookingGroup the booking group containing the booking
     * @param refresh      a Runnable to refresh the view after removal
     * @return an HBox representing the booking cell
     */
    public static HBox createBookingCell(BookingDetails booking, int index, BookingGroup bookingGroup, Runnable refresh) { // Added BookingGroup parameter
        HBox bookingCell = new HBox(10);
        bookingCell.setAlignment(Pos.CENTER_LEFT);
        bookingCell.setPrefHeight(80);
        bookingCell.setPrefWidth(500);
        bookingCell.setStyle("-fx-background-color: #e0ffe4; -fx-border-color: #122023; -fx-border-width: 1px;"
                + " -fx-padding: 10px 20px; -fx-background-radius: 5px;");

        VBox details = new VBox(5);
        Label eventLabel = new Label(booking.getEventName());
        eventLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label detailsLabel = new Label(booking.getRoom() + " on " + booking.getDate() +
                " from " + booking.getStartTime() + " to " + booking.getEndTime());
        detailsLabel.setStyle("-fx-font-size: 14px;");
        details.getChildren().addAll(eventLabel, detailsLabel);

        Button removeButton = new Button("✕");
        removeButton.setStyle("-fx-background-color: white; -fx-text-fill: #122023; " +
                "-fx-font-weight: bold; -fx-padding: 2px 6px; " +
                "-fx-border-radius: 50%; -fx-background-radius: 50%;");
        removeButton.setOnAction(e -> {
            bookingGroup.getBookings().remove(index); // Use BookingGroup instead of BookingDetails
            refresh.run();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        bookingCell.getChildren().addAll(details, spacer, removeButton);
        return bookingCell;
    }

    /**
     * Creates a text field with a prompt and specified dimensions.
     *
     * @param prompt the prompt text to display in the text field
     * @param width  the preferred width of the text field
     * @param height the preferred height of the text field
     * @return a configured TextField instance
     */
    public static TextField createTextField(String prompt, double width, double height) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setPrefWidth(width);
        field.setPrefHeight(height);
        field.setStyle("-fx-font-size: 14px;");
        return field;
    }

    /**
     * Creates a choice box (dropdown) populated with times.
     * <p>
     * The times are formatted as HH:00 and HH:30 for each hour in the range 10 to 23,
     * with an additional "00:00" option appended.
     * </p>
     *
     * @param width  the preferred width of the choice box
     * @param height the preferred height of the choice box
     * @return a configured ChoiceBox containing time options
     */
    public static ChoiceBox<String> createTimeDropdown(double width, double height) {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.setPrefWidth(width);
        choiceBox.setPrefHeight(height);
        IntStream.rangeClosed(10, 23).forEach(hour -> {
            choiceBox.getItems().add(String.format("%02d:00", hour));
            choiceBox.getItems().add(String.format("%02d:30", hour));
        });
        choiceBox.getItems().add("00:00");
        return choiceBox;
    }

    /**
     * Adds a date chip (a small labeled UI element) to a flow pane.
     * <p>
     * The chip displays a date and includes a remove button that, when clicked,
     * removes the date from the selected dates list and updates the daily bookings.
     * </p>
     *
     * @param flowPane      the flow pane where the date chip will be added
     * @param date          the date to display on the chip
     * @param selectedDates the list of currently selected dates
     * @param dailyBookings a map of daily bookings that will be updated accordingly
     */
    public static void addDateChip(FlowPane flowPane, LocalDate date, List<LocalDate> selectedDates,
                                   Map<LocalDate, List<BookingManager.RoomBooking>> dailyBookings) {
        Label dateChip = new Label(date.toString());
        dateChip.setPadding(new Insets(5, 10, 5, 10));
        dateChip.setStyle("-fx-background-color: #00B232; -fx-text-fill: white; " +
                "-fx-background-radius: 15px; -fx-font-weight: bold; -fx-font-size: 14px;");

        HBox chipBox = new HBox(5);
        chipBox.setAlignment(Pos.CENTER_LEFT);
        chipBox.getChildren().add(dateChip);

        Button removeButton = new Button("×");
        removeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 16px;");
        removeButton.setOnAction(e -> {
            selectedDates.remove(date);
            dailyBookings.remove(date);
            flowPane.getChildren().clear();
            selectedDates.forEach(d -> addDateChip(flowPane, d, selectedDates, dailyBookings));
        });
        chipBox.getChildren().add(removeButton);

        chipBox.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 0, 1);");
        flowPane.getChildren().add(chipBox);
    }

    /**
     * Creates a calendar container UI component for selecting multiple dates.
     * <p>
     * This component includes an embedded calendar view, control buttons to clear or confirm selections,
     * and updates a flow pane with selected dates using date chips.
     * </p>
     *
     * @param initialDate      the initial date to display/selected in the calendar
     * @param selectedDatesPane the FlowPane to display the selected date chips
     * @param selectedDates    a list of selected dates to be updated
     * @param dailyBookings    a map of daily bookings which is updated when the dates change
     * @return a VBox containing the calendar container UI
     */
    public static VBox createCalendarContainer(LocalDate initialDate, FlowPane selectedDatesPane,
                                               List<LocalDate> selectedDates,
                                               Map<LocalDate, List<BookingManager.RoomBooking>> dailyBookings) {
        VBox calendarContainer = new VBox(15);
        calendarContainer.setAlignment(Pos.CENTER);

        StackPane tempStackPane = new StackPane();
        FullCalendarView dateSelectionCalendar = new FullCalendarView(YearMonth.from(initialDate), tempStackPane, null);
        dateSelectionCalendar.setSelectionMode(true);
        dateSelectionCalendar.setMultiSelectEnabled(true);
        dateSelectionCalendar.addSelectedDate(initialDate);
        Node compactCalendar = dateSelectionCalendar.createEmbeddedCalendarView();

        VBox calendarBox = (VBox) compactCalendar;
        calendarBox.setPrefSize(300, 250);
        calendarBox.setMaxSize(300, 250);
        calendarBox.setStyle("-fx-background-color: #e0ffe4; -fx-background-radius: 8px; -fx-padding: 10px;");

        HBox calendarButtons = new HBox(15);
        calendarButtons.setAlignment(Pos.CENTER);
        calendarButtons.setPadding(new Insets(15, 0, 5, 0));

        Button clearDatesButton = new Button("Clear");
        clearDatesButton.setPrefSize(100, 30);
        clearDatesButton.setStyle("-fx-font-size: 14px;");
        clearDatesButton.setOnAction(e -> {
            dateSelectionCalendar.clearAllSelections();
            selectedDates.clear();
            selectedDates.add(initialDate);
            dailyBookings.clear();
            dateSelectionCalendar.addSelectedDate(initialDate);
            selectedDatesPane.getChildren().clear();
            selectedDates.forEach(d -> addDateChip(selectedDatesPane, d, selectedDates, dailyBookings));
        });

        Button doneDatesButton = new Button("Done");
        doneDatesButton.setPrefSize(100, 30);
        doneDatesButton.setStyle("-fx-font-size: 14px;");
        doneDatesButton.setOnAction(e -> {
            calendarContainer.setVisible(false);
            calendarContainer.setManaged(false);
        });

        calendarButtons.getChildren().addAll(clearDatesButton, doneDatesButton);

        Label calendarTitle = new Label("Select Multiple Dates");
        calendarTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 0 0 10 0;");

        dateSelectionCalendar.setOnDateSelectionChanged(() -> {
            Set<LocalDate> calendarSelectedDates = dateSelectionCalendar.getSelectedDates();
            selectedDates.clear();
            selectedDates.addAll(calendarSelectedDates);
            dailyBookings.keySet().removeIf(d -> !selectedDates.contains(d));
            selectedDatesPane.getChildren().clear();
            selectedDates.forEach(d -> addDateChip(selectedDatesPane, d, selectedDates, dailyBookings));
        });

        calendarContainer.getChildren().addAll(calendarTitle, calendarBox, calendarButtons);
        return calendarContainer;
    }

    /**
     * Displays an alert dialog with the specified title and message.
     *
     * @param title   the title of the alert dialog
     * @param message the message content to display
     */
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
