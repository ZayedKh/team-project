package lancaster.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class BookingsController implements Initializable {
    @FXML
    private TextField clientInput;

    @FXML
    private TextField clientEmailInput;

    @FXML
    private TextField clientTelephoneInput;

    @FXML
    private TextField clientAddressInput;

    @FXML
    private TextField eventNameInput;

    @FXML
    private ComboBox<String> eventTypeBox;

    @FXML
    private DatePicker eventDatePicker;

    @FXML
    private DatePicker eventEndDatePicker;

    @FXML
    private ComboBox<String> startTimeBox;

    @FXML
    private ComboBox<String> selectEndTime;

    @FXML
    private Button addBookingButton;

    @FXML
    private ComboBox<String> selectVenue;

    @FXML
    private ComboBox<String> selectConfiguration;

    @FXML
    private ComboBox<String> extraRoom;

    @FXML
    private ComboBox<String> roomConfiguration;  //name of the choicebox to select room configuration

    @FXML
    private Button confirmBookingButton;

    @FXML
    private CheckBox policyCheckbox;

    @FXML
    private CheckBox extraRoomCheckBox;

    @FXML
    private CheckBox multidayCheckbox;

    @FXML
    private CheckBox fullDayCheckbox;

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

    boolean extraRoomSelected = false;
    boolean fullDaySelected = false;
    boolean multidaySelected = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        eventEndDatePicker.setDisable(true);

        selectVenue.getItems().addAll(
                "Main Hall", "Small Hall", "Rehearsal Space", "The Green Room", "Brontë Boardroom", "Dickens Den",
                "Poe Parlor", "Globe Room", "Chekhov Chamber"
        );

        eventTypeBox.getItems().addAll("Event", "Meeting", "Conference", "Workshop");


        extraRoom.getItems().addAll(
                "The Green Room", "Brontë Boardroom", "Dickens Den",
                "Poe Parlor", "Globe Room", "Chekhov Chamber"
        );

        extraRoom.setDisable(true);


        selectVenue.setOnAction(e -> handleVenueConfiguration());
        //extraRoom.setOnAction(e -> handleRoomConfiguration());

        extraRoomCheckBox.setOnAction(e -> {
            extraRoomSelected = extraRoomCheckBox.isSelected();
            if (extraRoomSelected) {
                extraRoom.setDisable(false);
            } else {
                extraRoom.setDisable(true);
            }
        });


        multidayCheckbox.setOnAction(e -> {
            multidaySelected = multidayCheckbox.isSelected();
            if (multidaySelected) {
                eventEndDatePicker.setDisable(false);
                startTimeBox.setDisable(true);
                selectEndTime.setDisable(true);
                if (fullDayCheckbox.isSelected()) {
                    fullDaySelected = false;
                    fullDayCheckbox.setSelected(fullDaySelected);
                }
            } else {
                startTimeBox.setDisable(false);
                selectEndTime.setDisable(false);
                eventEndDatePicker.setDisable(true);
            }
        });



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
                    multidayCheckbox.setSelected(multidaySelected);
                }
            } else {
                startTimeBox.setDisable(false);
                selectEndTime.setDisable(false);
            }
        });

        for (int hour = 10; hour <= 23; hour++) {
            String time = String.format("%02d:00", hour);
            startTimeBox.getItems().add(time);
            selectEndTime.getItems().add(time);
        }
    }


    private void checkEndTime() {
        String startTime = startTimeBox.getValue();
        String endTime = selectEndTime.getValue();

        if (startTime != null && endTime != null) {
            int startHour = Integer.parseInt(startTime.split(":")[0]);
            int endHour = Integer.parseInt(endTime.split(":")[0]);

            if (endHour < startHour) {
                // Display an error message or take appropriate action
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Time Selection");
                alert.setHeaderText(null);
                alert.setContentText("End time cannot be before start time.");
                alert.showAndWait();
            }
        }
    }

    private void handleVenueConfiguration() {
        String selected = selectVenue.getValue();
        selectConfiguration.getItems().clear();

        if (selected == null) return;

        String venue = selected.trim();

        if (venue.equalsIgnoreCase("Main Hall")) {
            selectConfiguration.getItems().addAll("Stalls", "Stalls and Balconies", "Main Seating Only");
        } else if (venue.equalsIgnoreCase("Small Hall")) {
            selectConfiguration.getItems().add("Stalls");
        } else {
            selectConfiguration.getItems().addAll("Classroom", "Presentation", "Boardroom");
        }
    }

    private void handleRoomConfiguration() {
        String room = extraRoom.getValue();
        roomConfiguration.getItems().clear();

        if (room != null) {
            roomConfiguration.getItems().addAll("Classroom", "Boardroom", "Presentation");
        }
    }

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