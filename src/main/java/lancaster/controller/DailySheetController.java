package lancaster.controller;

import lancaster.model.Booking;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lancaster.model.Event;
import lancaster.utils.DBUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller for the Daily Sheet view.
 * Displays all bookings for a selected day in a table format.
 */
public class DailySheetController implements Initializable {

    @FXML
    private Label lblDate; // Displays the currently selected date

    @FXML
    private TableView<Event> tableDaily; // Table to display booking entries

    @FXML
    private TableColumn<Booking, String> colSpace; // Column for booked space

    @FXML
    private TableColumn<Booking, String> colStartTime; // Column for start time

    @FXML
    private TableColumn<Booking, String> colEndTime; // Column for end time

    @FXML
    private TableColumn<Event, String> colRoom;

    @FXML
    private TableColumn<Event, String> colName;

    @FXML
    private TableColumn<Booking, String> colBy; // Column for the person who booked

    @FXML
    private TableColumn<Booking, String> colConfig; // Column for room configuration

    private LocalDate date; // The date for which bookings are displayed

    /**
     * Initializes the table columns with the appropriate Booking property bindings.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up table columns
        colRoom.setCellValueFactory(new PropertyValueFactory<>("room_name"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("start_time"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("end_time"));
    }

    /**
     * Sets the selected date and updates the label and table content accordingly.
     *
     * @param date the date to display bookings for
     */
    public void setDate(LocalDate date) {
        this.date = date;
        lblDate.setText("Daily Sheet for " + date.toString());
        loadDailyData();
    }

    /**
     * Loads booking data for the given date.
     */
    private void loadDailyData() {
        try {
            DBUtils db = new DBUtils();
            ObservableList<Event> events = FXCollections.observableArrayList(
                    db.getEventForDay(Date.valueOf(this.date))
            );
            tableDaily.setItems(events);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error getting daily data");
        }

    }

    /**
     * Handles the back button click by returning to the selection pane.
     *
     * @param event the action event triggered by clicking the back button
     */
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lancaster/ui/SelectionPane.fxml"));
            Parent selectionPane = loader.load();
            Stage stage = (Stage) lblDate.getScene().getWindow();
            stage.getScene().setRoot(selectionPane);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
