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

public class DailySheetController implements Initializable {

    @FXML
    private Label lblDate;

    @FXML
    private TableView<Booking> tableDaily;

    @FXML
    private TableColumn<Booking, String> colSpace;

    @FXML
    private TableColumn<Booking, String> colStartTime;

    @FXML
    private TableColumn<Booking, String> colEndTime;

    @FXML
    private TableColumn<Booking, String> colBy;

    @FXML
    private TableColumn<Booking, String> colConfig;

    private LocalDate date;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up table columns
        colSpace.setCellValueFactory(new PropertyValueFactory<>("space"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        colBy.setCellValueFactory(new PropertyValueFactory<>("bookedBy"));
        colConfig.setCellValueFactory(new PropertyValueFactory<>("configuration"));
    }

    /**
     * Called by FullCalendarView to set the date for which the daily sheet is shown
     */
    public void setDate(LocalDate date) {
        this.date = date;
        lblDate.setText("Daily Sheet for " + date.toString());
        loadDailyData();
    }

    /**
     * Loads daily data for the given date
     * For now, dummy data is provided.
     */
    private void loadDailyData() {
        ObservableList<Booking> bookings = FXCollections.observableArrayList(
                new Booking("The Green Room", date, date, LocalTime.parse("10:00"), LocalTime.parse("11:00"), "Alice", "Standard"),
                new Booking("Brontë Boardroom", date, date, LocalTime.parse("11:30"), LocalTime.parse("12:30"), "Bob", "Video Conferencing"),
                new Booking("Dickens Den", date, date, LocalTime.parse("13:00"), LocalTime.parse("14:00"), "Charlie", "Projector")
        );
        tableDaily.setItems(bookings);
    }

    @FXML
    private void handleBack(ActionEvent event) {
        // Return to the report calendar view
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lancaster/ui/SelectionPane.fxml"));
            Parent selectionPane = loader.load();
            // Retrieve the current stage and replace its scene root
            Stage stage = (Stage) lblDate.getScene().getWindow();
            stage.getScene().setRoot(selectionPane);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}