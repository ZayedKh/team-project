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
    private TextField eventNameInput;

    @FXML
    private ComboBox<String> eventTypeBox;

    @FXML
    private DatePicker eventDatePicker;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectVenue.getItems().addAll("Main Hall", "Small Hall", "Rehearsal Space");

        extraRoom.getItems().addAll(
                "The Green Room", "Brontë Boardroom", "Dickens Den",
                "Poe Parlor", "Globe Room", "Chekhov Chamber"
        );

        selectVenue.setOnAction(e -> handleVenueConfiguration());
        extraRoom.setOnAction(e -> handleRoomConfiguration());
    }

    private void handleVenueConfiguration() {
        String venue = selectVenue.getValue();
        selectConfiguration.getItems().clear();

        if (venue == null) return;

        switch (venue) {
            case "Main Hall":
                selectConfiguration.getItems().add("Stalls and Balconies");
                selectConfiguration.setValue("Stalls and Balconies");
                break;
            case "Small Hall":
                selectConfiguration.getItems().add("Stalls");
                selectConfiguration.setValue("Stalls");
                break;
            case "Rehearsal Space":
                selectConfiguration.getItems().add("No seating configuration required");
                selectConfiguration.setValue("No seating configuration required");
                break;
        }
    }

    private void handleRoomConfiguration() {
        String room = extraRoom.getValue();
        roomConfiguration.getItems().clear();

        if (room != null) {
            roomConfiguration.getItems().addAll("Classroom", "Boardroom", "Presentation");
        }
    }
}