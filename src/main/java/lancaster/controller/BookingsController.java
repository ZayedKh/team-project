package lancaster.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import lancaster.utils.DBUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ResourceBundle;

/**
 * Controller class for managing event bookings in the Lancaster application.
 * This class handles the user interface logic for creating and confirming bookings,
 * including venue selection, date and time picking, and client information input.
 */
public class BookingsController implements Initializable {

    // **UI Elements for Client Information**
    @FXML
    private TextField clientInput;           // Field for client's name

    @FXML
    private TextField clientEmailInput;      // Field for client's email

    @FXML
    private TextField clientTelephoneInput;  // Field for client's telephone number

    @FXML
    private TextField clientAddressInput;    // Field for client's address

    // **UI Elements for Event Details**
    @FXML
    private TextField eventNameInput;        // Field for event name

    @FXML
    private ComboBox<String> eventTypeBox;   // Dropdown for selecting event type

    @FXML
    private DatePicker eventDatePicker;      // Date picker for selecting event date

    @FXML
    private ComboBox<String> startTimeBox;   // Dropdown for selecting start time

    @FXML
    private ComboBox<String> selectEndTime;  // Dropdown for selecting end time

    // **UI Elements for Venue and Configuration**
    @FXML
    private ComboBox<String> selectVenue;    // Dropdown for selecting the venue

    @FXML
    private ComboBox<String> selectConfiguration;  // Dropdown for venue configuration

    @FXML
    private ComboBox<String> selectExtraConfiguration;  // Dropdown for extra room configuration

    @FXML
    private ComboBox<String> extraRoom;      // Dropdown for selecting an extra room

    @FXML
    private ComboBox<String> roomConfiguration;  // Dropdown for room configuration selection

    // **Buttons**
    @FXML
    private Button addBookingButton;         // Not used in current code

    @FXML
    private Button addEventButton;           // Not used in current code

    @FXML
    private Button confirmBookingButton;     // Button to confirm the booking

    // **Checkboxes**
    @FXML
    private CheckBox policyCheckbox;         // Checkbox for policy agreement

    @FXML
    private CheckBox extraRoomCheckBox;      // Checkbox to enable extra room selection

    @FXML
    private CheckBox fullDayCheckbox;        // Checkbox for full-day booking

    @FXML
    private Label total;                     // Label for total cost

    @FXML
    private Label venueCost;                 // Label for venue cost

    @FXML
    private Label duration;                  // Label for event duration

    @FXML
    private Label extraRoomCost;             // Label for extra room cost

    @FXML
    private Label additionalServices;        // Label for additional services cost

    @FXML
    private Label tax;                       // Label for tax amount

    @FXML
    private VBox eventCreate;                // Not used in current code

    boolean extraRoomSelected = false;       // Tracks if an extra room is selected
    boolean fullDaySelected = false;         // Tracks if full-day option is selected
    boolean multidaySelected = false;        // Not used in current code

    /**
     * Initializes the booking controller after its root element has been completely processed.
     * Sets up the UI components, populates combo boxes with options, and configures booking handlers.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Commented-out database integration for fetching venue names
//        DBUtils dbUtils;
//        try {
//            dbUtils = new DBUtils();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        List<String> roomNames = dbUtils.getRoomNames();
//        selectVenue.getItems().addAll(roomNames);

        // Populate venue selection combo box with available venues
        selectVenue.getItems().addAll(
                "Main Hall", "Small Hall", "Rehearsal Space", "The Green Room", "Brontë Boardroom", "Dickens Den",
                "Poe Parlor", "Globe Room", "Chekhov Chamber"
        );

        // Populate event type combo box
        eventTypeBox.getItems().addAll("Event", "Meeting", "Conference", "Workshop");

        // Populate extra room combo box (initially disabled)
        extraRoom.getItems().addAll(
                "The Green Room", "Brontë Boardroom", "Dickens Den",
                "Poe Parlor", "Globe Room", "Chekhov Chamber"
        );
        extraRoom.setDisable(true);

        // Set up event handlers for venue selection to update configuration options
        selectVenue.setOnAction(e -> handleVenueConfiguration(selectVenue, selectConfiguration));
        extraRoom.setOnAction(e -> handleVenueConfiguration(extraRoom, selectExtraConfiguration));

        // Handle extra room checkbox to enable/disable extra room selection
        extraRoomCheckBox.setOnAction(e -> {
            extraRoomSelected = extraRoomCheckBox.isSelected();
            if (extraRoomSelected) {
                extraRoom.setDisable(false);
                selectExtraConfiguration.setDisable(false);
            } else {
                extraRoom.setDisable(true);
                selectExtraConfiguration.setDisable(true);
            }
        });

        // Handle full day checkbox to set default times and disable time selection
        fullDayCheckbox.setOnAction(e -> {
            fullDaySelected = fullDayCheckbox.isSelected();
            if (fullDaySelected) {
                startTimeBox.setValue("10:00");
                selectEndTime.setValue("23:00");
                startTimeBox.setDisable(true);
                selectEndTime.setDisable(true);
            } else {
                startTimeBox.setDisable(false);
                selectEndTime.setDisable(false);
                startTimeBox.setValue(null);
                selectEndTime.setValue(null);
                startTimeBox.setPromptText("Select a start time");
                selectEndTime.setPromptText("Select an end time");
            }
        });

        // Populate time selection combo boxes with hourly slots from 10:00 to 23:00
        for (int hour = 10; hour <= 23; hour++) {
            String time = String.format("%02d:00", hour);
            startTimeBox.getItems().add(time);
            selectEndTime.getItems().add(time);
        }

        // Set up confirm booking button to validate inputs and create booking
        confirmBookingButton.setOnAction(event -> {
            // Check if all required fields are filled
            if (clientInput.getText().isEmpty() || clientEmailInput.getText().isEmpty()
                    || clientTelephoneInput.getText().isEmpty() || clientAddressInput.getText().isEmpty()
                    || eventTypeBox.getValue() == null || eventNameInput.getText().isEmpty()
                    || eventDatePicker.getValue() == null || startTimeBox.getValue() == null
                    || selectEndTime.getValue() == null || selectVenue.getValue() == null
                    || (extraRoomCheckBox.isSelected() && extraRoom.getValue() == null)
                    || (extraRoomCheckBox.isSelected() && selectExtraConfiguration.getValue() == null)
                    || !policyCheckbox.isSelected() || selectConfiguration.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please enter all fields");
                alert.show();
            } else {
                try {
                    // Create a new DBUtils instance to interact with the database
                    DBUtils db = new DBUtils();
                    // Check if there is a booking conflict for the selected venue, date, and time
                    if (!db.bookingConflict(Date.valueOf(eventDatePicker.getValue()), Time.valueOf(startTimeBox.getValue() + ":00"), Time.valueOf(selectEndTime.getValue() + ":00"), db.getRoomId(selectVenue.getValue()))) {
                        // Create a new booking with the provided details
                        db.createBooking(db.getRoomId(selectVenue.getValue()), Date.valueOf(eventDatePicker.getValue()), Date.valueOf(eventDatePicker.getValue()),
                                clientInput.getText(), clientEmailInput.getText(), clientTelephoneInput.getText(),
                                clientAddressInput.getText(), "pending");
                        // Create a new event associated with the booking
                        db.createEvent(db.getRoomId(selectVenue.getValue()), 1, eventNameInput.getText(), Date.valueOf(eventDatePicker.getValue()),
                                Time.valueOf(startTimeBox.getValue() + ":00"), Time.valueOf(selectEndTime.getValue() + ":00"));
                        // Show confirmation alert
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("You have created a booking");
                        alert.show();
                    } else {
                        // Show error alert if there is a conflict
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Already an event at this time");
                        alert.show();
                    }
                } catch (SQLException | IOException | ClassNotFoundException e) {
                    // TODO: Handle exceptions more gracefully, e.g., show an error message to the user
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Checks if the selected end time is after the start time.
     * Displays an error alert if the end time is before the start time.
     */
    private void checkEndTime() {
        String startTime = startTimeBox.getValue();
        String endTime = selectEndTime.getValue();

        if (startTime != null && endTime != null) {
            int startHour = Integer.parseInt(startTime.split(":")[0]);
            int endHour = Integer.parseInt(endTime.split(":")[0]);

            if (endHour < startHour) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Time Selection");
                alert.setHeaderText(null);
                alert.setContentText("End time cannot be before start time.");
                alert.showAndWait();
            }
        }
    }

    /**
     * Updates the configuration combo box based on the selected venue.
     * Different venues have different configuration options.
     *
     * @param venues The combo box for selecting the venue.
     * @param config The combo box for selecting the configuration.
     */
    private void handleVenueConfiguration(ComboBox<String> venues, ComboBox<String> config) {
        String selected = venues.getValue();
        config.getItems().clear();

        if (selected == null) return;

        String venue = selected.trim();

        if (venue.equalsIgnoreCase("Main Hall")) {
            config.getItems().addAll("Stalls", "Stalls and Balconies", "Main Seating Only");
        } else if (venue.equalsIgnoreCase("Small Hall")) {
            config.getItems().add("Stalls");
        } else {
            config.getItems().addAll("Classroom", "Presentation", "Boardroom");
        }
    }

    /**
     * Stores the input values from the UI components.
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