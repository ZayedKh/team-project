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

    // Mock data for bookings
    private Map<String, Map<LocalDateTime, VenueStatus>> bookings = new HashMap<>();
    private double scaleFactor = 1.0;
    private LocalDate currentDate = LocalDate.now();
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
    private SelectionPaneController selectionPaneController;

    public void setSelectionPaneController(SelectionPaneController selectionPaneController) {
        this.selectionPaneController = selectionPaneController;
    }

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

        // Add tooltip
        Tooltip tooltip = new Tooltip(room + "\n" +
                dateTime.format(DateTimeFormatter.ofPattern("HH:mm")) + " - " +
                dateTime.plusHours(1).format(DateTimeFormatter.ofPattern("HH:mm")) + "\n" +
                status.getDescription());
        Tooltip.install(cellPane, tooltip);

        return cellPane;
    }

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

    @FXML
    private void handlePreviousDay() {
        currentDate = currentDate.minusDays(1);
        datePicker.setValue(currentDate);
        updateCurrentDateLabel();
        populateCalendar();
    }

    @FXML
    private void handleNextDay() {
        currentDate = currentDate.plusDays(1);
        datePicker.setValue(currentDate);
        updateCurrentDateLabel();
        populateCalendar();
    }

    @FXML
    private void handleAddBooking() {
        if (selectionPaneController != null) {
            selectionPaneController.showBookingPane();
        } else {
            System.out.println("SelectionPaneController is not set.");
        }
    }


    private void updateCurrentDateLabel() {
        currentDateLabel.setText(currentDate.format(dateFormatter));
    }

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

    private VenueStatus getBookingStatus(String room, LocalDateTime dateTime) {
        if (bookings.containsKey(room) && bookings.get(room).containsKey(dateTime)) {
            return bookings.get(room).get(dateTime);
        }
        return VenueStatus.AVAILABLE; // Default status
    }


    // Helper class to store booking data in each cell
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
