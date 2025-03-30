package lancaster.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.util.stream.IntStream;

public class AnchorPaneNode extends AnchorPane {

    private LocalDate date;
    private StackPane mainView;
    private Node calendarView;

    public AnchorPaneNode(StackPane mainView, Node calendarView, Node... children) {
        super(children);
        this.mainView = mainView;
        this.calendarView = calendarView;

        String originalStyle = "-fx-background-color: #e0ffe4; -fx-border-color: #122023; -fx-border-width: 1px;" + "-fx-background-radius: 5px; -fx-border-radius: 3px;";

        this.setOnMouseEntered(e -> this.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #122023; -fx-border-width: 1px;"+ "-fx-background-radius: 5px; -fx-border-radius: 3px;"));
        this.setOnMouseExited(e -> this.setStyle(originalStyle));

        this.setOnMouseClicked(e -> showBookingScreen());
    }

    private void showBookingScreen() {
        BorderPane bookingScreen = new BorderPane();
        bookingScreen.setPadding(new Insets(20));
        bookingScreen.setStyle("-fx-background-color: #122023;");

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10));

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> mainView.getChildren().setAll(calendarView));
        Label titleLabel = new Label("Select a Room");

        StackPane titleWrapper = new StackPane();
        titleWrapper.setStyle("-fx-background-color: #2ECC40; -fx-padding: 5px 15px; -fx-background-radius: 5px;");
        titleWrapper.setPadding(new Insets(5));
        titleWrapper.getChildren().add(titleLabel);

        titleLabel.setStyle("-fx-font-size: 16px;");
        header.getChildren().addAll(backButton, titleWrapper);
        bookingScreen.setTop(header);

        // Center: VBox listing the rooms/halls.
        VBox roomList = new VBox(10);
        roomList.setPadding(new Insets(20));
        String[] rooms = {"The Green Room", "Brontë Boardroom", "Dickens Den",
                "Poe Parlor", "Globe Room", "Chekhov Chamber", "Main Hall"};
        for (String room : rooms) {
            // Use HBox for each room cell.
            HBox roomCell = new HBox();
            roomCell.setAlignment(Pos.CENTER_LEFT);
            roomCell.setPrefHeight(40);
            roomCell.setStyle("-fx-background-color: #e0ffe4; -fx-border-color: #122023; -fx-border-width: 1px;" + "-fx-padding: 5px 15px; -fx-background-radius: 5px;");
            roomCell.setMaxWidth(Double.MAX_VALUE);
            roomCell.setPadding(new Insets(0, 10, 0, 10));

            Label roomLabel = new Label(room);
            roomLabel.setStyle("-fx-font-size: 14px;");
            roomCell.getChildren().add(roomLabel);

            roomCell.setOnMouseEntered(e -> roomCell.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #122023; -fx-border-width: 1px;"
                    + "-fx-padding: 5px 15px; -fx-background-radius: 5px;"));

            roomCell.setOnMouseExited(e -> roomCell.setStyle("-fx-background-color: #e0ffe4; -fx-border-color: #122023; -fx-border-width: 1px;"
                    + "-fx-padding: 5px 15px; -fx-background-radius: 5px;"));
            // Clicking the room cell navigates to the client booking page.
            roomCell.setOnMouseClicked(e -> showClientNamePage(room));
            roomList.getChildren().add(roomCell);
        }
        bookingScreen.setCenter(roomList);

        mainView.getChildren().setAll(bookingScreen);
    }

    private void showClientNamePage(String room) {
        // Use a BorderPane for a consistent layout.
        BorderPane clientPage = new BorderPane();
        clientPage.setPadding(new Insets(20));
        clientPage.setStyle("-fx-background-color: #122023;");

        // Top header with back button and title.
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10));
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showBookingScreen());
        Label title = new Label("Booking for " + room);
        title.setStyle("-fx-font-size: 16px;");

        StackPane titleWrapper = new StackPane();
        titleWrapper.setStyle("-fx-background-color: #2ECC40; -fx-padding: 5px 15px; -fx-background-radius: 5px;");
        titleWrapper.getChildren().add(title);
        titleWrapper.setPadding(new Insets(5));

        header.getChildren().addAll(backButton, titleWrapper);
        clientPage.setTop(header);

        // Center: Form and All Day option in a VBox.
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(15);
        form.setAlignment(Pos.CENTER);

        Label eventLabel = new Label("Event Name:");
        TextField eventField = new TextField();
        eventField.setPromptText("Enter Event Name");
        eventField.setPrefWidth(200);

        Label clientLabel = new Label("Client Name:");
        TextField clientField = new TextField();
        clientField.setPromptText("Enter Client Name");
        clientField.setPrefWidth(200);

        Label startTimeLabel = new Label("Start Time:");
        ChoiceBox<String> startTimeBox = new ChoiceBox<>();
        populateTimeDropdown(startTimeBox);

        Label endTimeLabel = new Label("End Time:");
        ChoiceBox<String> endTimeBox = new ChoiceBox<>();
        populateTimeDropdown(endTimeBox);

        form.add(eventLabel, 0, 0);
        form.add(eventField, 1, 0);
        form.add(clientLabel, 0, 1);
        form.add(clientField, 1, 1);
        form.add(startTimeLabel, 0, 2);
        form.add(startTimeBox, 1, 2);
        form.add(endTimeLabel, 0, 3);
        form.add(endTimeBox, 1, 3);

        // Add a CheckBox for "All Day" booking.
        CheckBox allDayCheckBox = new CheckBox("All Day");
        // When selected, disable the start and end time dropdowns.
        allDayCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            startTimeBox.setDisable(isSelected);
            endTimeBox.setDisable(isSelected);
        });

        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.getChildren().addAll(form, allDayCheckBox);

        StackPane formWrapper = new StackPane();
        formWrapper.setStyle("-fx-background-color: #e0ffe4; -fx-padding: 5px 5px; -fx-background-radius: 5px;  -fx-max-width: 300px;");
        formWrapper.getChildren().add(formContainer);

        clientPage.setCenter(formWrapper);

        // Bottom: Submit button.
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String clientName = clientField.getText();
            String eventName = eventField.getText();
            String startTime = startTimeBox.getValue();
            String endTime = endTimeBox.getValue();

            // If All Day is selected, override the chosen times.
            if (allDayCheckBox.isSelected()) {
                startTime = "10:00";
                endTime = "00:00";
            }
            // Validate required fields.
            if (clientName.isEmpty() || eventName.isEmpty() || startTime == null || endTime == null) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }
            if (startTime.compareTo(endTime) >= 0 && !endTime.equals("00:00")) {
                // Note: When endTime is "00:00" it represents midnight of the next day.
                showAlert("Error", "End time must be later than start time.");
                return;
            }

            System.out.println("Booked '" + eventName + "' for " + clientName +
                    " in " + room + " on " + date + " from " + startTime + " to " + endTime);
            mainView.getChildren().setAll(calendarView);
        });
        HBox buttonBox = new HBox(submitButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        clientPage.setBottom(buttonBox);

        mainView.getChildren().setAll(clientPage);
    }

    // Updated to list times from 10:00 to 23:30 and add "00:00" as the last option.
    private void populateTimeDropdown(ChoiceBox<String> choiceBox) {
        IntStream.rangeClosed(10, 23).forEach(hour -> {
            choiceBox.getItems().add(String.format("%02d:00", hour));
            choiceBox.getItems().add(String.format("%02d:30", hour));
        });
        choiceBox.getItems().add("00:00");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
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
