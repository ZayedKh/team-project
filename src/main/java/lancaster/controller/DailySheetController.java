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
 * Controller class for managing the daily sheet view in the Lancaster application.
 * This class handles the display of events for a specific date, including daily sheet setup and data loading.
 */
public class DailySheetController implements Initializable {

    // **UI Elements**
    @FXML
    private Label lblDate;                   // Label to display the selected date

    @FXML
    private TableView<Event> tableDaily;     // Table to display daily events

    @FXML
    private TableColumn<Event, String> colStartTime;  // Column for event start time

    @FXML
    private TableColumn<Event, String> colEndTime;    // Column for event end time

    @FXML
    private TableColumn<Event, String> colRoom;       // Column for event room

    @FXML
    private TableColumn<Event, String> colName;       // Column for event name

    private LocalDate date;                  // The date for which the daily sheet is shown

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up the table columns to display event data.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up table columns to bind to Event properties
        colRoom.setCellValueFactory(new PropertyValueFactory<>("room_name"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("start_time"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("end_time"));
    }

    /**
     * Sets the date for which the daily sheet is to be displayed.
     * Updates the date label and loads the corresponding event data.
     *
     * @param date The date for which to show the daily sheet.
     */
    public void setDate(LocalDate date) {
        this.date = date;
        lblDate.setText("Daily Sheet for " + date.toString());
        loadDailyData();
    }

    /**
     * Loads the event data for the specified date from the database and populates the table.
     * If an error occurs during data retrieval, a RuntimeException is thrown.
     */
    private void loadDailyData() {
        try {
            DBUtils db = new DBUtils();
            // Retrieve events for the specified date and wrap them in an ObservableList
            ObservableList<Event> events = FXCollections.observableArrayList(
                    db.getEventForDay(Date.valueOf(this.date))
            );
            // Set the table's items to the list of events
            tableDaily.setItems(events);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            // Wrap and rethrow the exception to be handled by the caller or JVM
            throw new RuntimeException("Error getting daily data", e);
        }
    }

    /**
     * Loads the SelectionPane FXML and sets it as the root of the current scene.
     *
     * @param event The ActionEvent triggered by the back button.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            // Load the SelectionPane FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lancaster/ui/SelectionPane.fxml"));
            Parent selectionPane = loader.load();
            // Retrieve the current stage and replace its scene root with the loaded pane
            Stage stage = (Stage) lblDate.getScene().getWindow();
            stage.getScene().setRoot(selectionPane);
        } catch (IOException ex) {
            // Print the stack trace for debugging purposes
            ex.printStackTrace();
        }
    }
}