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

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * Controller for the Daily Sheet view.
 * Displays all bookings for a selected day in a table format.
 */
public class DailySheetController implements Initializable {

    @FXML
    private Label lblDate; // Displays the currently selected date

    @FXML
    private TableView<Booking> tableDaily; // Table to display booking entries

    @FXML
    private TableColumn<Booking, String> colSpace; // Column for booked space

    @FXML
    private TableColumn<Booking, String> colStartTime; // Column for start time

    @FXML
    private TableColumn<Booking, String> colEndTime; // Column for end time

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
        colSpace.setCellValueFactory(new PropertyValueFactory<>("space"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        colBy.setCellValueFactory(new PropertyValueFactory<>("bookedBy"));
        colConfig.setCellValueFactory(new PropertyValueFactory<>("configuration"));
    }

    /**
     * Sets the selected date and updates the label and table content accordingly.
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
        ObservableList<Booking> bookings = FXCollections.observableArrayList(
                new Booking("The Green Room", date, date, LocalTime.parse("10:00"), LocalTime.parse("11:00"), "Alice", "Standard"),
                new Booking("Brontë Boardroom", date, date, LocalTime.parse("11:30"), LocalTime.parse("12:30"), "Bob", "Video Conferencing"),
                new Booking("Dickens Den", date, date, LocalTime.parse("13:00"), LocalTime.parse("14:00"), "Charlie", "Projector")
        );

        // Populate the table with booking entries
        tableDaily.setItems(bookings);
    }

    /**
     * Handles the back button click by returning to the selection pane.
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
