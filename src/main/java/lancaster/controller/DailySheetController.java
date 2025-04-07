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

public class DailySheetController implements Initializable {

    @FXML
    private Label lblDate;

    @FXML
    private TableView<Event> tableDaily;

    @FXML
    private TableColumn<Event, String> colStartTime;

    @FXML
    private TableColumn<Event, String> colEndTime;

    @FXML
    private TableColumn<Event, String> colRoom;

    @FXML
    private TableColumn<Event, String> colName;

    private LocalDate date;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up table columns
        colRoom.setCellValueFactory(new PropertyValueFactory<>("room_name"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("start_time"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("end_time"));
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
    private void loadDailyData()  {
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