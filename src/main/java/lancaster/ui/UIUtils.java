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

public class UIUtils {

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

    public static TextField createTextField(String prompt, double width, double height) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setPrefWidth(width);
        field.setPrefHeight(height);
        field.setStyle("-fx-font-size: 14px;");
        return field;
    }

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

    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}