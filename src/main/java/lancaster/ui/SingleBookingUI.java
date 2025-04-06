package lancaster.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SingleBookingUI {
    private StackPane mainView;
    private Node calendarView;
    private AnchorPaneNode anchorPaneNode;
    private BookingManager bookingManager;

    public SingleBookingUI(StackPane mainView, Node calendarView, AnchorPaneNode anchorPaneNode, BookingManager bookingManager) {
        this.mainView = mainView;
        this.calendarView = calendarView;
        this.anchorPaneNode = anchorPaneNode;
        this.bookingManager = bookingManager;
    }

    public void showSingleBookingScreen() {
        BorderPane bookingScreen = new BorderPane();
        bookingScreen.setPadding(new Insets(30));
        bookingScreen.setPrefSize(800, 600);
        bookingScreen.setStyle("-fx-background-color: #122023;");

        // Create the header with the "Pending Bookings" button
        HBox header = UIUtils.createHeader("Select a Room", e -> new BookingTypeSelectionUI(mainView, calendarView, anchorPaneNode, bookingManager).show(), bookingManager.getBookingGroup());

        // Add the "Pending Bookings" button to the header
        Button pendingBookingsButton = new Button("Pending Bookings");
        pendingBookingsButton.setPrefSize(150, 40);
        pendingBookingsButton.setStyle("-fx-font-size: 16px;");
        pendingBookingsButton.setOnAction(e -> new PendingBookingsUI(mainView, calendarView, anchorPaneNode, bookingManager).show());

        // Add the button to the right side of the header
        header.getChildren().add(pendingBookingsButton);
        HBox.setHgrow(pendingBookingsButton, Priority.NEVER); // Ensure it doesn't stretch
        HBox.setMargin(pendingBookingsButton, new Insets(0, 0, 0, 20)); // Add some spacing from the edge

        bookingScreen.setTop(header);

        VBox roomList = new VBox(15);
        roomList.setPadding(new Insets(30));
        roomList.setMaxWidth(600);
        roomList.setAlignment(Pos.CENTER);

        String[] rooms = {"The Green Room", "Brontë Boardroom", "Dickens Den",
                "Poe Parlor", "Globe Room", "Chekhov Chamber", "Main Hall",
                "Small Hall", "Rehearsal Space"};

        for (String room : rooms) {
            HBox roomCell = UIUtils.createRoomCell(room, e -> showSingleClientNamePage(room));
            roomList.getChildren().add(roomCell);
        }

        StackPane centerWrapper = new StackPane(roomList);
        centerWrapper.setAlignment(Pos.CENTER);
        bookingScreen.setCenter(centerWrapper);

        mainView.getChildren().setAll(bookingScreen);
    }

    private void showSingleClientNamePage(String room) {
        BorderPane clientPage = new BorderPane();
        clientPage.setPadding(new Insets(30));
        clientPage.setPrefSize(800, 600);
        clientPage.setStyle("-fx-background-color: #122023;");

        HBox header = UIUtils.createHeader("Booking for " + room, e -> showSingleBookingScreen(), bookingManager.getBookingGroup());
        clientPage.setTop(header);

        GridPane form = new GridPane();
        form.setHgap(20);
        form.setVgap(20);
        form.setAlignment(Pos.CENTER);

        TextField eventField = UIUtils.createTextField("Enter Event Name", 300, 40);
        TextField clientField = UIUtils.createTextField("Enter Client Name", 300, 40);
        ChoiceBox<String> startTimeBox = UIUtils.createTimeDropdown(300, 40);
        ChoiceBox<String> endTimeBox = UIUtils.createTimeDropdown(300, 40);

        form.add(new Label("Event Name:") {{ setStyle("-fx-font-size: 16px;"); }}, 0, 0);
        form.add(eventField, 1, 0);
        form.add(new Label("Client Name:") {{ setStyle("-fx-font-size: 16px;"); }}, 0, 1);
        form.add(clientField, 1, 1);
        form.add(new Label("Start Time:") {{ setStyle("-fx-font-size: 16px;"); }}, 0, 2);
        form.add(startTimeBox, 1, 2);
        form.add(new Label("End Time:") {{ setStyle("-fx-font-size: 16px;"); }}, 0, 3);
        form.add(endTimeBox, 1, 3);
        form.add(new Label("Edit Configuration:") {{ setStyle("-fx-font-size: 16px;"); }}, 0, 4);

        HBox roomConfigBox = new HBox(10);
        roomConfigBox.setAlignment(Pos.CENTER_LEFT);

        // Create the room configuration dropdown
        ChoiceBox<String> roomTypeDropdown = new ChoiceBox<>();
        roomTypeDropdown.getItems().addAll("Presentation", "Boardroom", "Classroom");
        roomTypeDropdown.setStyle("-fx-font-size: 16px;");
        roomTypeDropdown.setPrefWidth(300);

        // Add the dropdown to the layout if the room is not Main Hall or Small Hall
        if (room.equals("Main Hall") || room.equals("Small Hall")) {
            Button roomConfigButton = new Button("Edit Seating Configuration");
            roomConfigButton.setStyle("-fx-font-size: 16px;");
            roomConfigButton.setPrefSize(300, 40);
            roomConfigBox.getChildren().add(roomConfigButton);
        } else {
            roomConfigBox.getChildren().add(roomTypeDropdown);
        }

        form.add(roomConfigBox, 1, 4);

        // Checkbox for all-day events
        CheckBox allDayCheckBox = new CheckBox("All Day");
        allDayCheckBox.setStyle("-fx-font-size: 16px;");
        allDayCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            startTimeBox.setDisable(isSelected);
            endTimeBox.setDisable(isSelected);
        });

        VBox formContainer = new VBox(25);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(25));
        formContainer.getChildren().addAll(form, allDayCheckBox);

        StackPane centerWrapper = new StackPane(formContainer);
        centerWrapper.setStyle("-fx-background-color: #e0ffe4; -fx-padding: 20px; -fx-background-radius: 8px;");
        clientPage.setCenter(centerWrapper);

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(30, 0, 0, 0));

        Button addBookingButton = new Button("Add Booking");
        addBookingButton.setPrefSize(150, 40);
        addBookingButton.setStyle("-fx-font-size: 16px;");

        Button submitButton = new Button("Submit");
        submitButton.setPrefSize(150, 40);
        submitButton.setStyle("-fx-font-size: 16px;");

        // Runnable to create bookings
        Runnable createBookings = () -> {
            String clientName = clientField.getText();
            String eventName = eventField.getText();
            String startTime = startTimeBox.getValue();
            String endTime = endTimeBox.getValue();

            // Get the selected room configuration type
            String selectedRoomType = roomTypeDropdown.getValue();

            // Ensure that a room configuration type is selected
            if (selectedRoomType == null) {
                UIUtils.showAlert("Error", "Please select a seating configuration.");
                return;
            }

            if (allDayCheckBox.isSelected()) {
                startTime = "10:00";
                endTime = "00:00";
            }

            if (clientName.isEmpty() || eventName.isEmpty() || startTime == null || endTime == null) {
                UIUtils.showAlert("Error", "Please fill in all fields.");
                return;
            }

            if (startTime.compareTo(endTime) >= 0 && !endTime.equals("00:00")) {
                UIUtils.showAlert("Error", "End time must be later than start time.");
                return;
            }

            // Create the booking with room configuration
            bookingManager.createSingleBooking(anchorPaneNode.getDate(), room, startTime, endTime, eventName, clientName, selectedRoomType);
        };

        addBookingButton.setOnAction(e -> {
            createBookings.run();
            UIUtils.showAlert("Success", "Booking added to pending list.");
            showSingleBookingScreen();
        });

        submitButton.setOnAction(e -> {
            createBookings.run();
            bookingManager.getBookingGroup().submitAll();
            UIUtils.showAlert("Success", "Booking submitted.");
            mainView.getChildren().setAll(calendarView);
        });

        buttonBox.getChildren().addAll(addBookingButton, submitButton);
        clientPage.setBottom(buttonBox);

        mainView.getChildren().setAll(clientPage);
    }
}