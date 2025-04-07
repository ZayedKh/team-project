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
 * Controller class for handling the booking interface logic in the JavaFX application.
 * This includes setting up UI components, managing booking preferences like full-day or multi-day,
 * and dynamically loading room configurations.
 */
public class BookingsController implements Initializable {

    // Client Information Inputs
    @FXML
    private TextField clientInput;
    @FXML
    private TextField clientEmailInput;
    @FXML
    private TextField clientTelephoneInput;
    @FXML
    private TextField clientAddressInput;

    // Event Information Inputs
    @FXML
    private TextField eventNameInput;
    @FXML
    private ComboBox<String> eventTypeBox;
    @FXML
    private DatePicker eventDatePicker;
    @FXML
    private DatePicker eventEndDatePicker;

    // Time Selection Controls
    @FXML
    private ComboBox<String> startTimeBox;
    @FXML
    private ComboBox<String> selectEndTime;
    @FXML
    private ComboBox<String> selectExtraConfiguration;

    @FXML
    private ComboBox<String> extraRoom;

    // Venue and Room Configuration
    @FXML
    private ComboBox<String> selectVenue;
    @FXML
    private ComboBox<String> selectConfiguration;
    @FXML
    private ComboBox<String> roomConfiguration;

    // Booking Action Buttons
    @FXML
    private Button addBookingButton;
    @FXML
    private Button confirmBookingButton;
    @FXML
    private Button addEventButton;


    // Booking Options
    @FXML
    private CheckBox policyCheckbox;
    @FXML
    private CheckBox extraRoomCheckBox;
    @FXML
    private CheckBox multidayCheckbox;
    @FXML
    private CheckBox fullDayCheckbox;

    // Pricing Labels
    @FXML
    private Label total;
    @FXML
    private Label venueCost;
    @FXML
    private Label duration;
    @FXML
    private Label extraRoomCost;
    @FXML
    private Label additionalServices;
    @FXML
    private Label tax;

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

        //eventEndDatePicker.setDisable(true);

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
        selectVenue.setOnAction(e -> handleVenueConfiguration(selectVenue, selectConfiguration));
        extraRoom.setOnAction(e -> handleVenueConfiguration(extraRoom, selectExtraConfiguration));

        // Enable/disable extra room selection based on checkbox
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
                startTimeBox.setValue(null);
                selectEndTime.setValue(null);
                startTimeBox.setPromptText("Select a start time");
                selectEndTime.setPromptText("Select an end time");
            }
        });

        // Populate time options (10:00 - 23:00)
        for (int hour = 10; hour <= 23; hour++) {
            String time = String.format("%02d:00", hour);
            startTimeBox.getItems().add(time);
            selectEndTime.getItems().add(time);
        }

        confirmBookingButton.setOnAction(event -> {
            if (clientInput.getText().isEmpty() || clientEmailInput.getText().isEmpty()
                    || clientTelephoneInput.getText().isEmpty() || clientAddressInput.getText().isEmpty()
                    || eventTypeBox.getValue().isEmpty() || eventNameInput.getText().isEmpty()
                    || eventDatePicker.getValue() == null || startTimeBox.getValue().isEmpty()
                    || selectEndTime.getValue().isEmpty() || selectVenue.getValue().isEmpty()
                    || (extraRoomCheckBox.isSelected() && extraRoom.getValue().isEmpty())
                    || (extraRoomCheckBox.isSelected() && selectExtraConfiguration.getValue().isEmpty())
                    || !policyCheckbox.isSelected()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please enter all fields");
                alert.show();
            } else {
                try {
                    DBUtils db = new DBUtils();
                    if (!db.bookingConflict(Date.valueOf(eventDatePicker.getValue()), Time.valueOf(startTimeBox.getValue() + ":00"), Time.valueOf(selectEndTime.getValue() + ":00"))) {
                        db.createBooking(db.getRoomId(selectVenue.getValue()), Date.valueOf(eventDatePicker.getValue()), Date.valueOf(eventDatePicker.getValue()),
                                clientInput.getText(), clientEmailInput.getText(), clientTelephoneInput.getText(),
                                clientAddressInput.getText(), "pending");
                        db.createEvent(db.getRoomId(selectVenue.getValue()), 1, eventNameInput.getText(), Date.valueOf(eventDatePicker.getValue()),
                                Time.valueOf(startTimeBox.getValue() + ":00"), Time.valueOf(selectEndTime.getValue() + ":00"));
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("You have created a booking");
                        alert.show();
                    } else {
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
