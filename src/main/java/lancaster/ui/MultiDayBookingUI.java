package lancaster.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MultiDayBookingUI {
    private StackPane mainView;
    private Node calendarView;
    private AnchorPaneNode anchorPaneNode;
    private BookingManager bookingManager;

    public MultiDayBookingUI(StackPane mainView, Node calendarView, AnchorPaneNode anchorPaneNode, BookingManager bookingManager) {
        this.mainView = mainView;
        this.calendarView = calendarView;
        this.anchorPaneNode = anchorPaneNode;
        this.bookingManager = bookingManager;
    }

    public void showMultiDayBookingScreen() {
        BorderPane bookingScreen = new BorderPane();
        bookingScreen.setPadding(new Insets(30));
        bookingScreen.setPrefSize(800, 600);
        bookingScreen.setStyle("-fx-background-color: #122023;");

        // Create header with proper styling
        HBox header = UIUtils.createHeader(
                "Create Multi-day Booking",
                e -> new BookingTypeSelectionUI(mainView, calendarView, anchorPaneNode, bookingManager).show(),
                bookingManager.getBookingGroup()
        );
        bookingScreen.setTop(header);

        HBox mainContainer = new HBox(20);
        mainContainer.setAlignment(Pos.CENTER);

        GridPane form = new GridPane();
        form.setHgap(20);
        form.setVgap(20);
        form.setAlignment(Pos.CENTER);

        // Create the text fields with desired dimensions
        TextField eventField = UIUtils.createTextField("Enter Event Name", 300, 40);
        TextField clientField = UIUtils.createTextField("Enter Client Name", 300, 40);

        // Use labels with fixed minWidth to avoid truncation
        Label eventLabel = new Label("Event Name:");
        eventLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");
        Label clientLabel = new Label("Client Name:");
        clientLabel.setMinWidth(120); // Set a minimum width so it doesn't shorten
        clientLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");

        form.add(eventLabel, 0, 0);
        form.add(eventField, 1, 0);
        form.add(clientLabel, 0, 1);
        form.add(clientField, 1, 1);

        FlowPane selectedDatesPane = new FlowPane(10, 10);
        selectedDatesPane.setPrefWidth(400);
        selectedDatesPane.setPrefHeight(100);
        selectedDatesPane.setPadding(new Insets(10));
        selectedDatesPane.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 8px; " +
                "-fx-border-color: #2ECC40; -fx-border-width: 2px; -fx-border-radius: 8px;");
        bookingManager.getSelectedDates().clear();
        bookingManager.getSelectedDates().add(anchorPaneNode.getDate());
        bookingManager.getDailyBookings().clear();
        UIUtils.addDateChip(selectedDatesPane, anchorPaneNode.getDate(), bookingManager.getSelectedDates(), bookingManager.getDailyBookings());

        VBox calendarContainer = UIUtils.createCalendarContainer(anchorPaneNode.getDate(), selectedDatesPane, bookingManager.getSelectedDates(), bookingManager.getDailyBookings());
        calendarContainer.setVisible(true);
        calendarContainer.setManaged(true);

        VBox formContainer = new VBox(25);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(25));
        formContainer.getChildren().addAll(form, new Label("Selected Dates:") {{ setStyle("-fx-font-size: 16px; -fx-text-fill: black;"); }}, selectedDatesPane);

        StackPane formWrapper = new StackPane();
        formWrapper.setStyle("-fx-background-color: #e0ffe4; -fx-padding: 20px; -fx-background-radius: 8px;");
        formWrapper.getChildren().add(formContainer);
        formWrapper.setMaxWidth(450);
        formWrapper.setPrefWidth(450);

        mainContainer.getChildren().addAll(formWrapper, new StackPane(calendarContainer) {{ setStyle("-fx-padding: 15px;"); }});
        StackPane centerWrapper = new StackPane(mainContainer);
        centerWrapper.setAlignment(Pos.CENTER);
        bookingScreen.setCenter(centerWrapper);

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(30, 0, 0, 0));

        // Increase the next button's size and ensure it is visible
        Button nextButton = new Button("Next: Configure Dates");
        nextButton.setPrefSize(180, 45); // Adjusted size for better visibility
        nextButton.setStyle("-fx-font-size: 16px; -fx-background-color: #2ECC40; -fx-text-fill: white; -fx-background-radius: 8px;");
        nextButton.setOnAction(e -> {
            String clientName = clientField.getText();
            String eventName = eventField.getText();
            if (clientName.isEmpty() || eventName.isEmpty()) {
                UIUtils.showAlert("Error", "Please fill in all fields.");
                return;
            }
            showDateConfigurationScreen(clientName, eventName, 0);
        });

        buttonBox.getChildren().add(nextButton);
        bookingScreen.setBottom(buttonBox);

        mainView.getChildren().setAll(bookingScreen);
    }

    private void showDateConfigurationScreen(String clientName, String eventName, int currentDateIndex) {
        if (currentDateIndex >= bookingManager.getSelectedDates().size()) {
            new PendingBookingsUI(mainView, calendarView, anchorPaneNode, bookingManager).show();
            return;
        }

        LocalDate currentDate = bookingManager.getSelectedDates().get(currentDateIndex);
        BorderPane configScreen = new BorderPane();
        configScreen.setPadding(new Insets(30));
        configScreen.setPrefSize(800, 600);
        configScreen.setStyle("-fx-background-color: #122023;");

        HBox header = UIUtils.createHeader(
                "Configure " + currentDate + " (" + (currentDateIndex + 1) + "/" + bookingManager.getSelectedDates().size() + ")",
                e -> showMultiDayBookingScreen(),
                bookingManager.getBookingGroup()
        );
        configScreen.setTop(header);

        VBox mainContainer = new VBox(30);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setPadding(new Insets(30));
        // Removed maxWidth constraint to allow expansion
        mainContainer.setStyle(
                "-fx-background-color: #f4fff6; " +
                        "-fx-background-radius: 12px; " +
                        "-fx-border-color: #2ECC40; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 12px;"
        );
        VBox.setVgrow(mainContainer, Priority.ALWAYS); // Allow vertical growth

        // Container for multiple time/room configurations
        VBox configurationsContainer = new VBox(20);
        configurationsContainer.setAlignment(Pos.CENTER); // Center the configurations
        List<VBox> configurationBoxes = new ArrayList<>();

        // Method to create a new configuration box
        Runnable addConfigurationBox = () -> {
            VBox configBox = new VBox(15);
            configBox.setPadding(new Insets(10));
            configBox.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 8px; -fx-border-color: #2ECC40; -fx-border-width: 1px; -fx-border-radius: 8px;");

            GridPane timeForm = new GridPane();
            timeForm.setHgap(20);
            timeForm.setVgap(15);
            timeForm.setAlignment(Pos.CENTER); // Center the time form

            ChoiceBox<String> startTimeBox = UIUtils.createTimeDropdown(200, 40);
            ChoiceBox<String> endTimeBox = UIUtils.createTimeDropdown(200, 40);

            Label startLabel = new Label("Start Time:");
            startLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #122023;");
            Label endLabel = new Label("End Time:");
            endLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #122023;");

            timeForm.add(startLabel, 0, 0);
            timeForm.add(startTimeBox, 1, 0);
            timeForm.add(endLabel, 0, 1);
            timeForm.add(endTimeBox, 1, 1);

            CheckBox allDayCheckBox = new CheckBox("All Day");
            allDayCheckBox.setStyle("-fx-font-size: 16px; -fx-text-fill: #122023;");
            allDayCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                startTimeBox.setDisable(newVal);
                endTimeBox.setDisable(newVal);
            });
            timeForm.add(allDayCheckBox, 1, 2);

            FlowPane roomPane = new FlowPane();
            roomPane.setHgap(15);
            roomPane.setVgap(10);
            roomPane.setPadding(new Insets(10));
            roomPane.setPrefWrapLength(500); // Increased wrap length for better spread
            roomPane.setAlignment(Pos.CENTER); // Center the rooms
            roomPane.setStyle(
                    "-fx-background-color: #ffffff; " +
                            "-fx-background-radius: 10px; " +
                            "-fx-border-color: #2ECC40; " +
                            "-fx-border-width: 2px; " +
                            "-fx-border-radius: 10px;"
            );

            String[] rooms = {"The Green Room", "Brontë Boardroom", "Dickens Den",
                    "Poe Parlor", "Globe Room", "Chekhov Chamber", "Main Hall",
                    "Small Hall", "Rehearsal Space"};
            List<CheckBox> roomCheckBoxes = new ArrayList<>();
            for (String room : rooms) {
                CheckBox cb = new CheckBox(room);
                cb.setStyle("-fx-font-size: 15px; -fx-text-fill: #122023;");
                cb.setPrefWidth(220);
                roomPane.getChildren().add(cb);
                roomCheckBoxes.add(cb);
            }

            Button removeButton = new Button("Remove");
            removeButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-background-radius: 5px;");
            removeButton.setOnAction(e -> {
                configurationsContainer.getChildren().remove(configBox);
                configurationBoxes.remove(configBox);
            });

            configBox.getChildren().addAll(timeForm, roomPane, removeButton);
            configurationsContainer.getChildren().add(configBox);
            configurationBoxes.add(configBox);
        };

        // Add initial configuration
        addConfigurationBox.run();

        Button addConfigButton = new Button("Add Another Time Slot");
        addConfigButton.setStyle("-fx-font-size: 14px; -fx-background-color: #2ECC40; -fx-text-fill: white; -fx-background-radius: 8px;");
        addConfigButton.setOnAction(e -> addConfigurationBox.run());

        mainContainer.getChildren().addAll(
                new Label("Time and Room Selection") {{
                    setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #122023;");
                }},
                configurationsContainer,
                addConfigButton
        );

        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        StackPane centerWrapper = new StackPane(scrollPane);
        centerWrapper.setAlignment(Pos.CENTER);
        centerWrapper.setPadding(new Insets(30));
        BorderPane.setAlignment(centerWrapper, Pos.CENTER);
        configScreen.setCenter(centerWrapper);

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(30, 0, 0, 0));

        Button nextButton = new Button(currentDateIndex == bookingManager.getSelectedDates().size() - 1 ? "Finish" : "Next Date");
        nextButton.setPrefSize(160, 45);
        nextButton.setStyle("-fx-font-size: 16px; -fx-background-color: #2ECC40; -fx-text-fill: white; -fx-background-radius: 8px;");
        nextButton.setOnAction(e -> {
            List<BookingManager.RoomBooking> allBookings = new ArrayList<>();

            for (VBox configBox : configurationBoxes) {
                GridPane timeForm = (GridPane) configBox.getChildren().get(0);
                FlowPane roomPane = (FlowPane) configBox.getChildren().get(1);

                ChoiceBox<String> startTimeBox = (ChoiceBox<String>) timeForm.getChildren().get(1);
                ChoiceBox<String> endTimeBox = (ChoiceBox<String>) timeForm.getChildren().get(3);
                CheckBox allDayCheckBox = (CheckBox) timeForm.getChildren().get(4);

                String startTime = startTimeBox.getValue();
                String endTime = endTimeBox.getValue();

                if (allDayCheckBox.isSelected()) {
                    startTime = "10:00";
                    endTime = "00:00";
                }

                if (startTime == null || endTime == null) {
                    UIUtils.showAlert("Error", "Please select start and end times for all configurations.");
                    return;
                }

                if (startTime.compareTo(endTime) >= 0 && !endTime.equals("00:00")) {
                    UIUtils.showAlert("Error", "End time must be later than start time in all configurations.");
                    return;
                }

                List<String> selectedRooms = new ArrayList<>();
                for (Node node : roomPane.getChildren()) {
                    CheckBox cb = (CheckBox) node;
                    if (cb.isSelected()) {
                        selectedRooms.add(cb.getText());
                    }
                }

                if (selectedRooms.isEmpty()) {
                    UIUtils.showAlert("Error", "Please select at least one room for each configuration.");
                    return;
                }

                for (String room : selectedRooms) {
                    allBookings.add(new BookingManager.RoomBooking(room, startTime, endTime));
                }
            }

            bookingManager.getDailyBookings().put(currentDate, allBookings);

            if (currentDateIndex == bookingManager.getSelectedDates().size() - 1) {
                bookingManager.createMultiDayBookings(clientName, eventName);
                new PendingBookingsUI(mainView, calendarView, anchorPaneNode, bookingManager).show();
            } else {
                showDateConfigurationScreen(clientName, eventName, currentDateIndex + 1);
            }
        });

        buttonBox.getChildren().add(nextButton);
        configScreen.setBottom(buttonBox);

        mainView.getChildren().setAll(configScreen);
    }
    }