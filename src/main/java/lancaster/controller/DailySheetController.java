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
 * <p>
 * This class is responsible for displaying events scheduled for a specific date. It sets up the UI components,
 * such as the table and its columns, loads event data from the database for the provided date, and handles user
 * navigation back to the selection pane.
 * </p>
 */
public class DailySheetController implements Initializable {

    @FXML
    private Label lblDate;                   // Label to display the selected date

    @FXML
    private TableView<Event> tableDaily;     // TableView to display the list of events for the day

    @FXML
    private TableColumn<Event, String> colStartTime;  // Column displaying the event start time

    @FXML
    private TableColumn<Event, String> colEndTime;    // Column displaying the event end time

    @FXML
    private TableColumn<Event, String> colRoom;       // Column displaying the event room name

    @FXML
    private TableColumn<Event, String> colName;       // Column displaying the event name

    private LocalDate date;  // The date for which the daily sheet is to be shown

    /**
     * Initializes the controller after its root element has been completely processed.
     * <p>
     * This method configures the table columns to bind to the respective properties of the {@link Event} objects.
     * It is automatically called when the FXML is loaded.
     * </p>
     *
     * @param location  The location used to resolve relative paths for the root object, or {@code null} if unknown.
     * @param resources The resources used to localize the root object, or {@code null} if not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind table columns to Event object properties.
        colRoom.setCellValueFactory(new PropertyValueFactory<>("room_name"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("start_time"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("end_time"));
    }

    /**
     * Sets the date for which the daily sheet is to be displayed.
     * <p>
     * This method updates the date label to reflect the selected date and triggers the loading of corresponding
     * event data from the database.
     * </p>
     *
     * @param date the {@link LocalDate} representing the date to be displayed on the daily sheet.
     */
    public void setDate(LocalDate date) {
        this.date = date;
        lblDate.setText("Daily Sheet for " + date.toString());
        loadDailyData();
    }

    /**
     * Loads event data for the specified date from the database and populates the daily events table.
     * <p>
     * This method retrieves a list of events scheduled for the given date using the {@link DBUtils#getEventForDay(Date)} method,
     * wraps the list in an {@link ObservableList}, and sets it as the data source for the table view.
     * </p>
     *
     * @throws RuntimeException if there is an error retrieving event data from the database.
     */
    private void loadDailyData() {
        try {
            DBUtils db = new DBUtils();
            // Retrieve events for the specified date and wrap them in an ObservableList.
            ObservableList<Event> events = FXCollections.observableArrayList(
                    db.getEventForDay(Date.valueOf(this.date))
            );
            // Populate the table view with the retrieved events.
            tableDaily.setItems(events);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            // Wrap any exceptions into a RuntimeException to signal an unrecoverable error.
            throw new RuntimeException("Error getting daily data", e);
        }
    }

    /**
     * Handles the back button action to navigate from the daily sheet view to the selection pane.
     * <p>
     * This method loads the SelectionPane FXML file, retrieves the current stage, and updates the scene's root
     * node with the loaded pane, effectively navigating back.
     * </p>
     *
     * @param event the {@link ActionEvent} triggered by the back button.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            // Load the SelectionPane FXML file.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lancaster/ui/SelectionPane.fxml"));
            Parent selectionPane = loader.load();
            // Retrieve the current stage and update its scene's root.
            Stage stage = (Stage) lblDate.getScene().getWindow();
            stage.getScene().setRoot(selectionPane);
        } catch (IOException ex) {
            // Print the exception stack trace for debugging purposes.
            ex.printStackTrace();
        }
    }
}
