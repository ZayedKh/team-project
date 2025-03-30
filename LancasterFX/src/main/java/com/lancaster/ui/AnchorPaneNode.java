package com.lancaster.ui;

import com.lancaster.model.BookingDetails;
import com.lancaster.model.BookingGroup;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class AnchorPaneNode extends AnchorPane {

    private LocalDate date;
    private StackPane mainView;
    private Node calendarView;
    private BookingGroup bookingGroup = new BookingGroup();
    private List<LocalDate> selectedDates = new ArrayList<>();

    /**
     * Constructor requires the main container and the calendar view (to return back).
     * You may also pass child nodes if desired.
     */

    public AnchorPaneNode(StackPane mainView, Node calendarView, Node... children) {
        super(children);
        this.mainView = mainView;
        this.calendarView = calendarView;

        this.setPrefSize(120, 100);
        this.setMinSize(100, 80);

        String originalStyle = "-fx-background-color: #e0ffe4; -fx-border-color: #122023; -fx-border-width: 1px;"
                + " -fx-background-radius: 5px; -fx-border-radius: 3px;";

        String selectedStyle = "-fx-background-color: #2ECC40; -fx-border-color: #122023; -fx-border-width: 1px;"
                + " -fx-background-radius: 5px; -fx-border-radius: 3px;";

        String hoverStyle = "-fx-background-color: #FFFFFF; -fx-border-color: #122023; -fx-border-width: 1px;"
                + " -fx-background-radius: 5px; -fx-border-radius: 3px;";

        this.setOnMouseEntered(e -> {
            // Only change style if this date is not currently selected
            if (!selectedDates.contains(date)) {
                this.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #122023; -fx-border-width: 1px;"
                        + " -fx-background-radius: 5px; -fx-border-radius: 3px;");
            }
        });

        this.setOnMouseExited(e -> {
            // Only revert to original style if this date is not selected
            if (!selectedDates.contains(date)) {
                this.setStyle(originalStyle);
            }
        });

        this.setOnMouseClicked(e -> showBookingScreen());
    }
    public void showBookingScreen() {
        // Build a booking screen that overlays the calendar.
        BorderPane bookingScreen = new BorderPane();
        bookingScreen.setPadding(new Insets(30));
        bookingScreen.setPrefSize(800, 600); // Set preferred size for desktop
        bookingScreen.setStyle("-fx-background-color: #122023;");

        // Header with Back button.
        HBox header = new HBox(20); // Increased spacing
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15));

        Button backButton = new Button("Back");
        backButton.setPrefSize(100, 30); // Larger button
        backButton.setStyle("-fx-font-size: 14px;");
        backButton.setOnAction(e -> mainView.getChildren().setAll(calendarView));

        Label titleLabel = new Label("Select a Room");

        StackPane titleWrapper = new StackPane();
        titleWrapper.setStyle("-fx-background-color: #2ECC40; -fx-padding: 10px 25px; -fx-background-radius: 5px;");
        titleWrapper.setPadding(new Insets(8));
        titleWrapper.getChildren().add(titleLabel);
        titleLabel.setStyle("-fx-font-size: 20px;"); // Larger font

        header.getChildren().addAll(backButton, titleWrapper);
        bookingScreen.setTop(header);

        // Display booking summary if there are pending bookings
        if (!bookingGroup.getBookings().isEmpty()) {
            Label pendingLabel = new Label("Pending Bookings: " + bookingGroup.getBookings().size());
            pendingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #2ECC40;");
            header.getChildren().add(pendingLabel);

            Button viewPendingButton = new Button("View Pending");
            viewPendingButton.setPrefSize(120, 30);
            viewPendingButton.setStyle("-fx-font-size: 14px;");
            viewPendingButton.setOnAction(e -> showPendingBookings());
            header.getChildren().add(viewPendingButton);
        }

        // Center: A list of available rooms.
        VBox roomList = new VBox(15); // Increased spacing
        roomList.setPadding(new Insets(30));
        roomList.setMaxWidth(600); // Limit width for readability
        roomList.setAlignment(Pos.CENTER); // Center align for desktop view

        String[] rooms = {"The Green Room", "Brontë Boardroom", "Dickens Den",
                "Poe Parlor", "Globe Room", "Chekhov Chamber", "Main Hall"};

        for (String room : rooms) {
            HBox roomCell = new HBox();
            roomCell.setAlignment(Pos.CENTER_LEFT);
            roomCell.setPrefHeight(60); // Taller cells
            roomCell.setPrefWidth(500); // Fixed width for desktop
            roomCell.setStyle("-fx-background-color: #e0ffe4; -fx-border-color: #122023; -fx-border-width: 1px;"
                    + " -fx-padding: 10px 20px; -fx-background-radius: 5px;");
            roomCell.setMaxWidth(Double.MAX_VALUE);
            roomCell.setPadding(new Insets(0, 20, 0, 20)); // More horizontal padding

            Label roomLabel = new Label(room);
            roomLabel.setStyle("-fx-font-size: 18px;"); // Larger font
            roomCell.getChildren().add(roomLabel);

            roomCell.setOnMouseEntered(e -> roomCell.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #122023; -fx-border-width: 1px;"
                    + " -fx-padding: 10px 20px; -fx-background-radius: 5px;"));
            roomCell.setOnMouseExited(e -> roomCell.setStyle("-fx-background-color: #e0ffe4; -fx-border-color: #122023; -fx-border-width: 1px;"
                    + " -fx-padding: 10px 20px; -fx-background-radius: 5px;"));

            // When a room cell is clicked, show the client booking form.
            roomCell.setOnMouseClicked(e -> {
                // Reset selected dates when starting a new booking
                selectedDates.clear();
                selectedDates.add(date); // Add current date as default
                showClientNamePage(room);
            });
            roomList.getChildren().add(roomCell);
        }

        StackPane centerWrapper = new StackPane(roomList);
        centerWrapper.setAlignment(Pos.CENTER);
        bookingScreen.setCenter(centerWrapper);

        // Display the booking screen.
        mainView.getChildren().setAll(bookingScreen);
    }

    private void showPendingBookings() {
        BorderPane pendingPage = new BorderPane();
        pendingPage.setPadding(new Insets(30));
        pendingPage.setPrefSize(800, 600);
        pendingPage.setStyle("-fx-background-color: #122023;");

        // Header with Back button
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15));

        Button backButton = new Button("Back");
        backButton.setPrefSize(100, 30);
        backButton.setStyle("-fx-font-size: 14px;");
        backButton.setOnAction(e -> showBookingScreen());

        Label titleLabel = new Label("Pending Bookings");
        StackPane titleWrapper = new StackPane();
        titleWrapper.setStyle("-fx-background-color: #2ECC40; -fx-padding: 10px 25px; -fx-background-radius: 5px;");
        titleWrapper.setPadding(new Insets(8));
        titleWrapper.getChildren().add(titleLabel);
        titleLabel.setStyle("-fx-font-size: 20px;");

        header.getChildren().addAll(backButton, titleWrapper);
        pendingPage.setTop(header);

        // Display list of pending bookings
        VBox bookingsList = new VBox(15);
        bookingsList.setPadding(new Insets(30));
        bookingsList.setMaxWidth(600);
        bookingsList.setAlignment(Pos.CENTER);

        for (BookingDetails booking : bookingGroup.getBookings()) {
            HBox bookingCell = new HBox(10);
            bookingCell.setAlignment(Pos.CENTER_LEFT);
            bookingCell.setPrefHeight(80);
            bookingCell.setPrefWidth(500);
            bookingCell.setStyle("-fx-background-color: #e0ffe4; -fx-border-color: #122023; -fx-border-width: 1px;"
                    + " -fx-padding: 10px 20px; -fx-background-radius: 5px;");
            bookingCell.setMaxWidth(Double.MAX_VALUE);

            VBox details = new VBox(5);
            Label eventLabel = new Label(booking.getEventName());
            eventLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            Label detailsLabel = new Label(booking.getRoom() + " on " + booking.getDate() +
                    " from " + booking.getStartTime() + " to " + booking.getEndTime());
            detailsLabel.setStyle("-fx-font-size: 14px;");

            details.getChildren().addAll(eventLabel, detailsLabel);
            bookingCell.getChildren().add(details);

            bookingsList.getChildren().add(bookingCell);
        }

        StackPane centerWrapper = new StackPane(bookingsList);
        centerWrapper.setAlignment(Pos.CENTER);
        pendingPage.setCenter(centerWrapper);

        // Bottom buttons
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(30, 0, 0, 0));

        Button addMoreButton = new Button("Add More");
        addMoreButton.setPrefSize(150, 40);
        addMoreButton.setStyle("-fx-font-size: 16px;");
        addMoreButton.setOnAction(e -> showBookingScreen());

        Button submitAllButton = new Button("Submit All");
        submitAllButton.setPrefSize(150, 40);
        submitAllButton.setStyle("-fx-font-size: 16px;");
        submitAllButton.setOnAction(e -> {
            bookingGroup.submitAll();
            showAlert("Success", "All bookings have been submitted.");
            mainView.getChildren().setAll(calendarView);
        });

        Button clearAllButton = new Button("Clear All");
        clearAllButton.setPrefSize(150, 40);
        clearAllButton.setStyle("-fx-font-size: 16px;");
        clearAllButton.setOnAction(e -> {
            bookingGroup.getBookings().clear();
            showBookingScreen();
        });

        buttonBox.getChildren().addAll(addMoreButton, submitAllButton, clearAllButton);
        pendingPage.setBottom(buttonBox);

        mainView.getChildren().setAll(pendingPage);
    }

    private void showClientNamePage(String room) {
        BorderPane clientPage = new BorderPane();
        clientPage.setPadding(new Insets(30));
        clientPage.setPrefSize(800, 600);
        clientPage.setStyle("-fx-background-color: #122023;");

        // Header with Back button
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15));

        Button backButton = new Button("Back");
        backButton.setPrefSize(100, 30);
        backButton.setStyle("-fx-font-size: 14px;");
        backButton.setOnAction(e -> showBookingScreen());

        Label title = new Label("Booking for " + room);
        title.setStyle("-fx-font-size: 20px;");

        StackPane titleWrapper = new StackPane();
        titleWrapper.setStyle("-fx-background-color: #2ECC40; -fx-padding: 10px 25px; -fx-background-radius: 5px;");
        titleWrapper.setPadding(new Insets(8));
        titleWrapper.getChildren().add(title);

        header.getChildren().addAll(backButton, titleWrapper);
        clientPage.setTop(header);

        // Use HBox as main container to place form on left and calendar on right
        HBox mainContainer = new HBox(20);
        mainContainer.setAlignment(Pos.CENTER);

        // Left side: Booking form with fields and time selection
        GridPane form = new GridPane();
        form.setHgap(20);
        form.setVgap(20);
        form.setAlignment(Pos.CENTER);

        Label eventLabel = new Label("Event Name:");
        eventLabel.setStyle("-fx-font-size: 16px;");

        TextField eventField = new TextField();
        eventField.setPromptText("Enter Event Name");
        eventField.setPrefWidth(300);
        eventField.setPrefHeight(40);
        eventField.setStyle("-fx-font-size: 14px;");

        Label clientLabel = new Label("Client Name:");
        clientLabel.setStyle("-fx-font-size: 16px;");

        TextField clientField = new TextField();
        clientField.setPromptText("Enter Client Name");
        clientField.setPrefWidth(300);
        clientField.setPrefHeight(40);
        clientField.setStyle("-fx-font-size: 14px;");

        Label startTimeLabel = new Label("Start Time:");
        startTimeLabel.setStyle("-fx-font-size: 16px;");

        ChoiceBox<String> startTimeBox = new ChoiceBox<>();
        startTimeBox.setPrefWidth(300);
        startTimeBox.setPrefHeight(40);
        populateTimeDropdown(startTimeBox);

        Label endTimeLabel = new Label("End Time:");
        endTimeLabel.setStyle("-fx-font-size: 16px;");

        ChoiceBox<String> endTimeBox = new ChoiceBox<>();
        endTimeBox.setPrefWidth(300);
        endTimeBox.setPrefHeight(40);
        populateTimeDropdown(endTimeBox);

        form.add(eventLabel, 0, 0);
        form.add(eventField, 1, 0);
        form.add(clientLabel, 0, 1);
        form.add(clientField, 1, 1);
        form.add(startTimeLabel, 0, 2);
        form.add(startTimeBox, 1, 2);
        form.add(endTimeLabel, 0, 3);
        form.add(endTimeBox, 1, 3);

        // Time options
        CheckBox allDayCheckBox = new CheckBox("All Day");
        allDayCheckBox.setStyle("-fx-font-size: 16px;");
        allDayCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            startTimeBox.setDisable(isSelected);
            endTimeBox.setDisable(isSelected);
        });

        // Multi-day booking section with checkbox
        CheckBox multiDayCheckBox = new CheckBox("Multi-day Booking");
        multiDayCheckBox.setStyle("-fx-font-size: 16px;");

        // Create a flow pane to display selected dates
        // Create a flow pane to display selected dates
        FlowPane selectedDatesPane = new FlowPane(10, 10);
        selectedDatesPane.setPrefWidth(400);
        selectedDatesPane.setPrefHeight(100);
        selectedDatesPane.setPadding(new Insets(10));
// Improve the styling of the selected dates container
        selectedDatesPane.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 8px; " +
                "-fx-border-color: #2ECC40; -fx-border-width: 2px; -fx-border-radius: 8px;");

        // Add current date as default
        addDateChip(selectedDatesPane, date);

        VBox formContainer = new VBox(25);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(25));
        formContainer.getChildren().addAll(form, allDayCheckBox, multiDayCheckBox, new Label("Selected Dates:"), selectedDatesPane);

        StackPane formWrapper = new StackPane();
        formWrapper.setStyle("-fx-background-color: #e0ffe4; -fx-padding: 20px; -fx-background-radius: 8px;");
        formWrapper.getChildren().add(formContainer);

        // Set a max width for the form to ensure it doesn't expand too much
        formWrapper.setMaxWidth(450);
        formWrapper.setPrefWidth(450);

        // Right side: Calendar for date selection
        VBox calendarContainer = new VBox(15);
        calendarContainer.setAlignment(Pos.CENTER);
        calendarContainer.setVisible(false);
        calendarContainer.setManaged(false);

        // Calendar for date selection - create a small embedded calendar
        StackPane tempStackPane = new StackPane();
        FullCalendarView dateSelectionCalendar = new FullCalendarView(
                YearMonth.from(date),
                tempStackPane,
                null
        );
        dateSelectionCalendar.setSelectionMode(true);
        dateSelectionCalendar.setMultiSelectEnabled(true);
        dateSelectionCalendar.addSelectedDate(date);
        Node compactCalendar = dateSelectionCalendar.createEmbeddedCalendarView();

        // Make the calendar more compact and visually appealing
        VBox calendarBox = (VBox) compactCalendar;
        calendarBox.setPrefSize(300, 250);
        calendarBox.setMaxSize(300, 250);
        calendarBox.setStyle("-fx-background-color: #e0ffe4; -fx-background-radius: 8px; -fx-padding: 10px;");

        // Add clear and done buttons
        HBox calendarButtons = new HBox(15);
        calendarButtons.setAlignment(Pos.CENTER);
        calendarButtons.setPadding(new Insets(15, 0, 5, 0));

        Button clearDatesButton = new Button("Clear");
        clearDatesButton.setPrefSize(100, 30);
        clearDatesButton.setStyle("-fx-font-size: 14px;");
        clearDatesButton.setOnAction(e -> {
            dateSelectionCalendar.clearAllSelections();
            selectedDates.clear();
            selectedDates.add(date);
            dateSelectionCalendar.addSelectedDate(date);
            updateDateChips(selectedDatesPane);
        });

        Button doneDatesButton = new Button("Done");
        doneDatesButton.setPrefSize(100, 30);
        doneDatesButton.setStyle("-fx-font-size: 14px;");
        doneDatesButton.setOnAction(e -> {
            calendarContainer.setVisible(false);
            calendarContainer.setManaged(false);
        });

        calendarButtons.getChildren().addAll(clearDatesButton, doneDatesButton);

        // Calendar title
        Label calendarTitle = new Label("Select Multiple Dates");
        calendarTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 0 0 10 0;");

        // Add everything to the calendar container
        calendarContainer.getChildren().addAll(
                calendarTitle,
                calendarBox,
                calendarButtons
        );

        // Style calendar container
        StackPane calendarWrapper = new StackPane(calendarContainer);
        calendarWrapper.setStyle("-fx-padding: 15px;");

        // Sync selected dates when calendar selection changes
        dateSelectionCalendar.setOnDateSelectionChanged(() -> {
            Set<LocalDate> calendarSelectedDates = dateSelectionCalendar.getSelectedDates();
            selectedDates.clear();
            selectedDates.addAll(calendarSelectedDates);
            updateDateChips(selectedDatesPane);
        });

        // Toggle calendar visibility when multi-day checkbox is toggled
        multiDayCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            calendarContainer.setVisible(isSelected);
            calendarContainer.setManaged(isSelected);
            if (!isSelected) {
                // If multi-day is turned off, keep only the current date
                selectedDates.clear();
                selectedDates.add(date);
                dateSelectionCalendar.clearAllSelections();
                dateSelectionCalendar.addSelectedDate(date);
                updateDateChips(selectedDatesPane);
            }
        });

        // Add form and calendar to main container
        mainContainer.getChildren().addAll(formWrapper, calendarWrapper);

        StackPane centerWrapper = new StackPane(mainContainer);
        centerWrapper.setAlignment(Pos.CENTER);
        clientPage.setCenter(centerWrapper);

        // Bottom: Submit and Add Booking buttons
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(30, 0, 0, 0));

        Button addBookingButton = new Button("Add Booking");
        addBookingButton.setPrefSize(150, 40);
        addBookingButton.setStyle("-fx-font-size: 16px;");

        Button submitButton = new Button("Submit");
        submitButton.setPrefSize(150, 40);
        submitButton.setStyle("-fx-font-size: 16px;");

        // Common validation and booking creation logic
        Runnable createBookings = () -> {
            String clientName = clientField.getText();
            String eventName = eventField.getText();
            String startTime = startTimeBox.getValue();
            String endTime = endTimeBox.getValue();

            // If All Day is selected, override times
            if (allDayCheckBox.isSelected()) {
                startTime = "10:00";
                endTime = "00:00";
            }

            if (clientName.isEmpty() || eventName.isEmpty() || startTime == null || endTime == null) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }

            if (startTime.compareTo(endTime) >= 0 && !endTime.equals("00:00")) {
                showAlert("Error", "End time must be later than start time.");
                return;
            }

            // Create bookings for all selected dates
            for (LocalDate selectedDate : selectedDates) {
                BookingDetails booking = new BookingDetails();
                booking.setDate(selectedDate);
                booking.setRoom(room);
                booking.setStartTime(startTime);
                booking.setEndTime(endTime);
                booking.setEventName(eventName);
                booking.setClientName(clientName);

                bookingGroup.addBooking(booking);
            }
        };

        addBookingButton.setOnAction(e -> {
            createBookings.run();
            int count = selectedDates.size();
            showAlert("Success", count + " booking" + (count > 1 ? "s" : "") + " added to pending list.");
            showBookingScreen();
        });

        submitButton.setOnAction(e -> {
            createBookings.run();
            bookingGroup.submitAll();
            int count = selectedDates.size();
            showAlert("Success", count + " booking" + (count > 1 ? "s" : "") + " submitted.");
            mainView.getChildren().setAll(calendarView);
        });

        buttonBox.getChildren().addAll(addBookingButton, submitButton);
        clientPage.setBottom(buttonBox);

        mainView.getChildren().setAll(clientPage);
    }

    // Helper method to add a date chip to the flow pane
    // Helper method to add a date chip to the flow pane
    private void addDateChip(FlowPane flowPane, LocalDate date) {
        Label dateChip = new Label(date.toString());
        dateChip.setPadding(new Insets(5, 10, 5, 10));

        // Make the chips bold with a stronger green color
        dateChip.setStyle("-fx-background-color: #00B232; -fx-text-fill: white; " +
                "-fx-background-radius: 15px; -fx-font-weight: bold; -fx-font-size: 14px;");

        HBox chipBox = new HBox(5);
        chipBox.setAlignment(Pos.CENTER_LEFT);
        chipBox.getChildren().add(dateChip);

        // Add a remove button for dates other than the current one
        if (!date.equals(this.date)) {
            Button removeButton = new Button("×");
            removeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; " +
                    "-fx-font-weight: bold; -fx-font-size: 16px;");
            removeButton.setOnAction(e -> {
                selectedDates.remove(date);
                updateDateChips(flowPane);
            });
            chipBox.getChildren().add(removeButton);
        }

        // Add a small shadow to make the chips stand out more
        chipBox.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 0, 1);");

        flowPane.getChildren().add(chipBox);
    }
    // Helper method to update all date chips
    private void updateDateChips(FlowPane flowPane) {
        flowPane.getChildren().clear();
        for (LocalDate date : selectedDates) {
            addDateChip(flowPane, date);
        }
    }

    // Populate a ChoiceBox with time values from 10:00 to 23:30, plus "00:00" (midnight).
    private void populateTimeDropdown(ChoiceBox<String> choiceBox) {
        IntStream.rangeClosed(10, 23).forEach(hour -> {
            choiceBox.getItems().add(String.format("%02d:00", hour));
            choiceBox.getItems().add(String.format("%02d:30", hour));
        });
        choiceBox.getItems().add("00:00");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}