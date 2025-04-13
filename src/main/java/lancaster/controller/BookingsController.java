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
 * <p>
 * This class handles user interface logic for creating and confirming bookings,
 * including the selection of venues, choosing event dates and times, and inputting client information.
 * It configures UI components, validates user input, and interacts with the database via {@link DBUtils}.
 * </p>
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
    private ComboBox<String> selectConfiguration;  // Dropdown for selecting venue configuration

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
    private VBox eventCreate;                // Container related to event creation UI (not used in current code)

    // State tracking variables
    boolean extraRoomSelected = false;       // Flag to track if an extra room selection is active
    boolean fullDaySelected = false;         // Flag to track if full-day booking option is selected
    boolean multidaySelected = false;        // Placeholder for potential multi-day booking feature

    /**
     * Initializes the booking controller after its root element has been completely processed.
     * <p>
     * This method populates various UI components (combo boxes and date pickers) with available data such as venues,
     * event types, and time slots. It also sets up event handlers to manage user interactions, including:
     * <ul>
     *   <li>Configuring venue-specific options.</li>
     *   <li>Enabling or disabling extra room selection based on a checkbox.</li>
     *   <li>Setting default times and disabling time selection when the full-day option is chosen.</li>
     *   <li>Validating input fields and performing booking creation upon confirmation.</li>
     * </ul>
     * </p>
     *
     * @param url the location used to resolve relative paths for the root object, or {@code null} if unknown.
     * @param resourceBundle the resources used to localize the root object, or {@code null} if not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Uncomment and use database integration as needed to fetch dynamic venue names.
//        DBUtils dbUtils;
//        try {
//            dbUtils = new DBUtils();
//        } catch (SQLException | IOException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        List<String> roomNames = dbUtils.getRoomNames();
//        selectVenue.getItems().addAll(roomNames);

        // Populate venue selection combo box with available venues
        selectVenue.getItems().addAll(
                "Main Hall", "Small Hall", "Rehearsal Space", "The Green Room", "Brontë Boardroom", "Dickens Den",
                "Poe Parlor", "Globe Room", "Chekhov Chamber"
        );

        // Populate event type combo box with predefined event types
        eventTypeBox.getItems().addAll("Event", "Meeting", "Conference", "Workshop");

        // Populate extra room combo box (initially disabled)
        extraRoom.getItems().addAll(
                "The Green Room", "Brontë Boardroom", "Dickens Den",
                "Poe Parlor", "Globe Room", "Chekhov Chamber"
        );
        extraRoom.setDisable(true);

        // Set up event handlers to update venue configuration options
        selectVenue.setOnAction(e -> handleVenueConfiguration(selectVenue, selectConfiguration));
        extraRoom.setOnAction(e -> handleVenueConfiguration(extraRoom, selectExtraConfiguration));

        // Handle extra room checkbox to enable or disable extra room selection
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

        // Handle full-day checkbox to set default times and disable time selection controls
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

        // Populate time selection combo boxes with hourly time slots (from 10:00 to 23:00)
        for (int hour = 10; hour <= 23; hour++) {
            String time = String.format("%02d:00", hour);
            startTimeBox.getItems().add(time);
            selectEndTime.getItems().add(time);
        }

        // Set up confirm booking button with validation and database operations for booking creation
        confirmBookingButton.setOnAction(event -> {
            // Validate that all necessary fields are completed
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
                    // Initialize DBUtils to interact with the database
                    DBUtils db = new DBUtils();

                    // Check for booking conflicts at the selected venue, date, and time
                    if (!db.bookingConflict(
                            Date.valueOf(eventDatePicker.getValue()),
                            Time.valueOf(startTimeBox.getValue() + ":00"),
                            Time.valueOf(selectEndTime.getValue() + ":00"),
                            db.getRoomId(selectVenue.getValue())
                    )) {
                        // Create a new booking entry
                        db.createBooking(
                                db.getRoomId(selectVenue.getValue()),
                                Date.valueOf(eventDatePicker.getValue()),
                                Date.valueOf(eventDatePicker.getValue()),
                                clientInput.getText(),
                                clientEmailInput.getText(),
                                clientTelephoneInput.getText(),
                                clientAddressInput.getText(),
                                "pending"
                        );
                        // Create a new event associated with the booking
                        db.createEvent(
                                db.getRoomId(selectVenue.getValue()),
                                1,
                                eventNameInput.getText(),
                                Date.valueOf(eventDatePicker.getValue()),
                                Time.valueOf(startTimeBox.getValue() + ":00"),
                                Time.valueOf(selectEndTime.getValue() + ":00")
                        );
                        // Show confirmation alert
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("You have created a booking");
                        alert.show();
                    } else {
                        // Notify the user if a booking conflict exists
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Already an event at this time");
                        alert.show();
                    }
                } catch (SQLException | IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Checks whether the selected end time is after the start time.
     * <p>
     * If the end time occurs before the start time, an error dialog is displayed prompting the user to correct the selection.
     * </p>
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
     * Updates the configuration options available for the venue based on the selected venue.
     * <p>
     * For example, if "Main Hall" is selected, multiple configuration options (such as "Stalls" or "Balconies")
     * are provided; whereas for a smaller venue, only a single configuration might be available.
     * </p>
     *
     * @param venues the combo box containing venue selections.
     * @param config the combo box to be populated with configuration options based on the selected venue.
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
     * Stores and processes the input values from the UI components for later use.
     * <p>
     * This method retrieves the values entered by the user.
     * </p>
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
        // Further processing can be added here as needed.
    }
}
