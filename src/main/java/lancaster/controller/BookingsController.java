package lancaster.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for handling the booking interface logic in the JavaFX application.
 * This includes setting up UI components, managing booking preferences like full-day or multi-day,
 * and dynamically loading room configurations.
 */
public class BookingsController implements Initializable {

    // Client Information Inputs
    @FXML private TextField clientInput;
    @FXML private TextField clientEmailInput;
    @FXML private TextField clientTelephoneInput;
    @FXML private TextField clientAddressInput;

    // Event Information Inputs
    @FXML private TextField eventNameInput;
    @FXML private ComboBox<String> eventTypeBox;
    @FXML private DatePicker eventDatePicker;
    @FXML private DatePicker eventEndDatePicker;

    // Time Selection Controls
    @FXML private ComboBox<String> startTimeBox;
    @FXML private ComboBox<String> selectEndTime;

    // Venue and Room Configuration
    @FXML private ComboBox<String> selectVenue;
    @FXML private ComboBox<String> selectConfiguration;
    @FXML private ComboBox<String> extraRoom;
    @FXML private ComboBox<String> roomConfiguration;

    // Booking Action Buttons
    @FXML private Button addBookingButton;
    @FXML private Button confirmBookingButton;

    // Booking Options
    @FXML private CheckBox policyCheckbox;
    @FXML private CheckBox extraRoomCheckBox;
    @FXML private CheckBox multidayCheckbox;
    @FXML private CheckBox fullDayCheckbox;

    // Pricing Labels
    @FXML private Label total;
    @FXML private Label venueCost;
    @FXML private Label duration;
    @FXML private Label extraRoomCost;
    @FXML private Label additionalServices;
    @FXML private Label tax;

    // Option state flags
    private boolean extraRoomSelected = false;
    private boolean fullDaySelected = false;
    private boolean multidaySelected = false;

    /**
     * Initializes the controller after the root element has been completely processed.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Disable the end date picker initially (only used for multiday)
        eventEndDatePicker.setDisable(true);

        // Populate venue and event type dropdowns
        selectVenue.getItems().addAll(
                "Main Hall", "Small Hall", "Rehearsal Space", "The Green Room",
                "Brontë Boardroom", "Dickens Den", "Poe Parlor", "Globe Room", "Chekhov Chamber"
        );
        eventTypeBox.getItems().addAll("Event", "Meeting", "Conference", "Workshop");

        // Populate extra room options and disable selection initially
        extraRoom.getItems().addAll(
                "The Green Room", "Brontë Boardroom", "Dickens Den",
                "Poe Parlor", "Globe Room", "Chekhov Chamber"
        );
        extraRoom.setDisable(true);

        // Handle room configuration updates when a venue is selected
        selectVenue.setOnAction(e -> handleVenueConfiguration());

        // Enable/disable extra room selection based on checkbox
        extraRoomCheckBox.setOnAction(e -> {
            extraRoomSelected = extraRoomCheckBox.isSelected();
            extraRoom.setDisable(!extraRoomSelected);
        });

        // Enable multiday options and update UI accordingly
        multidayCheckbox.setOnAction(e -> {
            multidaySelected = multidayCheckbox.isSelected();
            eventEndDatePicker.setDisable(!multidaySelected);
            startTimeBox.setDisable(multidaySelected);
            selectEndTime.setDisable(multidaySelected);
            if (multidaySelected && fullDayCheckbox.isSelected()) {
                fullDaySelected = false;
                fullDayCheckbox.setSelected(false);
            }
        });

        // Handle full day checkbox behavior
        fullDayCheckbox.setOnAction(e -> {
            fullDaySelected = fullDayCheckbox.isSelected();
            if (fullDaySelected) {
                startTimeBox.setValue("10:00");
                selectEndTime.setValue("23:00");
                startTimeBox.setDisable(true);
                selectEndTime.setDisable(true);
                eventEndDatePicker.setDisable(true);
                if (multidaySelected) {
                    multidaySelected = false;
                    multidayCheckbox.setSelected(false);
                }
            } else {
                startTimeBox.setDisable(false);
                selectEndTime.setDisable(false);
            }
        });

        // Populate time options (10:00 - 23:00)
        for (int hour = 10; hour <= 23; hour++) {
            String time = String.format("%02d:00", hour);
            startTimeBox.getItems().add(time);
            selectEndTime.getItems().add(time);
        }
    }

    /**
     * Validates that the end time is not earlier than the start time.
     */
    private void checkEndTime() {
        String startTime = startTimeBox.getValue();
        String endTime = selectEndTime.getValue();

        if (startTime != null && endTime != null) {
            int startHour = Integer.parseInt(startTime.split(":")[0]);
            int endHour = Integer.parseInt(endTime.split(":")[0]);

            if (endHour < startHour) {
                // Show error dialog
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Time Selection");
                alert.setHeaderText(null);
                alert.setContentText("End time cannot be before start time.");
                alert.showAndWait();
            }
        }
    }

    /**
     * Updates the room configuration options based on selected venue.
     */
    private void handleVenueConfiguration() {
        String selected = selectVenue.getValue();
        selectConfiguration.getItems().clear();

        if (selected == null) return;

        switch (selected.trim()) {
            case "Main Hall" -> selectConfiguration.getItems().addAll("Stalls", "Stalls and Balconies", "Main Seating Only");
            case "Small Hall" -> selectConfiguration.getItems().add("Stalls");
            default -> selectConfiguration.getItems().addAll("Classroom", "Presentation", "Boardroom");
        }
    }

    /**
     * Updates the room configuration options for extra rooms.
     */
    private void handleRoomConfiguration() {
        String room = extraRoom.getValue();
        roomConfiguration.getItems().clear();

        if (room != null) {
            roomConfiguration.getItems().addAll("Classroom", "Boardroom", "Presentation");
        }
    }

    /**
     * Stores input values from the form
     */
    private void storeInputValues() {
        String clientName = clientInput.getText();
        String eventName = eventNameInput.getText();
        String eventType = eventTypeBox.getValue();
        String eventDate = eventDatePicker.getValue().toString();
        String startTime = startTimeBox.getValue();
        String endTime = selectEndTime.getValue();
        String venue = selectVenue.getValue();
        String configuration = selectConfiguration.getValue();
        String extraRoomValue = extraRoom.getValue();
        String roomConfig = roomConfiguration.getValue();
        boolean policyAgreed = policyCheckbox.isSelected();

    }
}
