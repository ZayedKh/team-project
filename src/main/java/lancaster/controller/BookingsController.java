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
    private ComboBox<String> extraRoom;

    @FXML
    private ComboBox<String> selectConfiguration;

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

    }
}