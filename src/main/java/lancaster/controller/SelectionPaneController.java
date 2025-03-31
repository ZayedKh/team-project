package com.lancaster.controller;

import com.lancaster.ui.FullCalendarView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.time.YearMonth;
import java.util.ResourceBundle;

public class SelectionPaneController implements Initializable {

    @FXML
    private StackPane mainContainer; // This is your view switcher

    private FullCalendarView calendarView;
    private Node selectionPane;  // The original selection pane (main menu)

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Assume the FXML's mainContainer initially contains the selection pane (menu).
        selectionPane = mainContainer.getChildren().get(0);

        // Build the calendar view (default booking mode) but do not add it to the main container yet.
        calendarView = new FullCalendarView(YearMonth.now(), mainContainer, selectionPane);
    }

    @FXML
    private void showBookingCalendar() {
        // Swap in the booking calendar view when Booking is clicked.
        mainContainer.getChildren().setAll(calendarView.getCalendarView());
    }

    @FXML
    private void showReportCalendar() {
        // Create a new calendar view in report mode.
        FullCalendarView reportCalendar = new FullCalendarView(YearMonth.now(), mainContainer, selectionPane, true);
        mainContainer.getChildren().setAll(reportCalendar.getCalendarView());
    }
}
