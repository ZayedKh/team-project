package com.lancaster.ui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

public class FullCalendarView {

    private ArrayList<AnchorPaneNode> allCalendarDays = new ArrayList<>(35);
    private StackPane mainView;
    private Text calendarTitle;
    private YearMonth currentYearMonth;
    private Node calendarView;

    /**
     * Create a styled calendar view for a given year and month.
     * @param yearMonth the year and month to display
     */
    public FullCalendarView(YearMonth yearMonth) {
        currentYearMonth = yearMonth;
        mainView = new StackPane();

        // Create a VBox to hold the calendar UI with a subtle background gradient.
        VBox calendarViewVBox = new VBox();
        calendarViewVBox.setStyle("-fx-background-color: linear-gradient(#E0E0E0, #F8F8F8); -fx-padding: 15px;");
        calendarView = calendarViewVBox;

        // Create the calendar grid pane.
        GridPane calendar = new GridPane();
        calendar.setPrefSize(600, 400);
        calendar.setGridLinesVisible(true);

        // Create rows and columns with AnchorPaneNodes for the calendar cells.
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                AnchorPaneNode ap = new AnchorPaneNode(mainView, calendarViewVBox);
                ap.setPrefSize(200, 200);
                ap.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #DDDDDD; -fx-border-width: 1px; " +
                        "-fx-background-radius: 5; -fx-border-radius: 5;");
                calendar.add(ap, j, i);
                allCalendarDays.add(ap);
            }
        }

        // Create days of the week labels with bold styling.
        Text[] dayNames = { new Text("Sunday"), new Text("Monday"), new Text("Tuesday"),
                new Text("Wednesday"), new Text("Thursday"), new Text("Friday"),
                new Text("Saturday") };
        GridPane dayLabels = new GridPane();
        dayLabels.setPrefWidth(600);
        int col = 0;
        for (Text txt : dayNames) {
            AnchorPane ap = new AnchorPane();
            ap.setPrefSize(200, 30);
            txt.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            AnchorPane.setBottomAnchor(txt, 5.0);
            AnchorPane.setLeftAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            ap.setStyle("-fx-background-color: #F0F0F0;");
            dayLabels.add(ap, col++, 0);
        }

        // Create the title bar with navigation buttons.
        calendarTitle = new Text();
        calendarTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Button previousMonth = new Button("<<");
        previousMonth.setOnAction(e -> previousMonth());
        Button nextMonth = new Button(">>");
        nextMonth.setOnAction(e -> nextMonth());
        HBox titleBar = new HBox(10, previousMonth, calendarTitle, nextMonth);
        titleBar.setAlignment(Pos.CENTER);
        titleBar.setStyle("-fx-padding: 10px;");

        // Populate the calendar with dates.
        populateCalendar(yearMonth);

        // Assemble the calendar view.
        calendarViewVBox.getChildren().addAll(titleBar, dayLabels, calendar);
        mainView.getChildren().add(calendarViewVBox);
    }

    /**
     * Populate the calendar cells with day numbers.
     * Dates not in the current month are styled in a lighter color.
     * @param yearMonth the year and month to render
     */
    public void populateCalendar(YearMonth yearMonth) {
        LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        // Roll back to the previous Sunday.
        while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY")) {
            calendarDate = calendarDate.minusDays(1);
        }
        for (AnchorPaneNode ap : allCalendarDays) {
            // Remove previous content if it exists.
            if (!ap.getChildren().isEmpty()) {
                ap.getChildren().remove(0);
            }
            Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));
            // Use a lighter color for dates outside the current month.
            if (calendarDate.getMonth() != yearMonth.getMonth()) {
                txt.setStyle("-fx-fill: #AAAAAA; -fx-font-size: 16px;");
            } else {
                txt.setStyle("-fx-fill: #333333; -fx-font-size: 16px;");
            }
            ap.setDate(calendarDate);
            AnchorPane.setTopAnchor(txt, 5.0);
            AnchorPane.setLeftAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            calendarDate = calendarDate.plusDays(1);
        }
        calendarTitle.setText(yearMonth.getMonth().toString() + " " + yearMonth.getYear());
    }

    private void previousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        populateCalendar(currentYearMonth);
    }

    private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        populateCalendar(currentYearMonth);
    }

    public StackPane getView() {
        return mainView;
    }

    public ArrayList<AnchorPaneNode> getAllCalendarDays() {
        return allCalendarDays;
    }

    public void setAllCalendarDays(ArrayList<AnchorPaneNode> allCalendarDays) {
        this.allCalendarDays = allCalendarDays;
    }
}
