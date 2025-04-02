package lancaster.ui;

import lancaster.controller.DailySheetController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FullCalendarView {

    private ArrayList<AnchorPaneNode> allCalendarDays = new ArrayList<>(35);
    private StackPane mainView;    // The main container
    private Text calendarTitle;
    private YearMonth currentYearMonth;
    private Node calendarView;     // The calendar view node (a VBox in this case)
    private Node selectionPane;    // The original selection pane to return to
    private GridPane calendar;     // Made into a class field for resizing

    // Calendar modes
    private boolean reportMode = false;
    private boolean selectionMode = false;
    private Set<LocalDate> selectedDates = new HashSet<>();
    private boolean multiSelectEnabled = false;

    // Callbacks
    private Runnable onDateSelectionCallback = null;

    /**
     * Construct a calendar view for the given month and year in booking mode.
     */
    public FullCalendarView(YearMonth yearMonth, StackPane mainView, Node selectionPane) {
        this(yearMonth, mainView, selectionPane, false);
    }

    /**
     * Overloaded constructor that allows report mode.
     *
     * @param yearMonth     the month and year to display
     * @param mainView      the main container (StackPane) used for swapping views
     * @param selectionPane the original selection pane (main menu) to return to
     * @param reportMode    if true, clicking on days will open the daily sheet instead of the booking screen.
     */
    public FullCalendarView(YearMonth yearMonth, StackPane mainView, Node selectionPane, boolean reportMode) {
        this.currentYearMonth = yearMonth;
        this.mainView = mainView;
        this.selectionPane = selectionPane;
        this.reportMode = reportMode;

        // Create a VBox to hold the calendar UI.
        VBox calendarViewVBox = new VBox();
        calendarViewVBox.setStyle("-fx-background-color: #122023; -fx-padding: 20px;");
        calendarViewVBox.setFillWidth(true);
        this.calendarView = calendarViewVBox;

        // Create a grid pane for the calendar cells with responsive sizing.
        calendar = new GridPane();
        // Allow the calendar to grow/shrink with the window
        calendar.setVgrow(calendar, javafx.scene.layout.Priority.ALWAYS);
        calendar.setHgrow(calendar, javafx.scene.layout.Priority.ALWAYS);
        calendar.setGridLinesVisible(true);

        // Set column constraints to make columns equally sized
        for (int i = 0; i < 7; i++) {
            javafx.scene.layout.ColumnConstraints colConstraint = new javafx.scene.layout.ColumnConstraints();
            colConstraint.setPercentWidth(100.0 / 7.0);
            calendar.getColumnConstraints().add(colConstraint);
        }

        // Set row constraints for 5 equal rows
        for (int i = 0; i < 5; i++) {
            javafx.scene.layout.RowConstraints rowConstraint = new javafx.scene.layout.RowConstraints();
            rowConstraint.setPercentHeight(100.0 / 5.0);
            calendar.getRowConstraints().add(rowConstraint);
        }

        // Create 35 cells (5 rows x 7 columns) for the calendar.
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                // Pass mainView and calendarView so the cell can swap to booking screens.
                AnchorPaneNode ap = new AnchorPaneNode(mainView, calendarView);
                // Let the cell expand with grid
                ap.setPrefSize(200, 150);
                ap.setStyle("-fx-background-color: #e0ffe4; -fx-border-color: #122023; -fx-border-width: 1px;"
                        + " -fx-background-radius: 5px; -fx-border-radius: 3px;");
                calendar.add(ap, j, i);
                allCalendarDays.add(ap);
            }
        }

        // Create a header row showing the day names.
        Text[] dayNames = {new Text("Sunday"), new Text("Monday"), new Text("Tuesday"),
                new Text("Wednesday"), new Text("Thursday"), new Text("Friday"), new Text("Saturday")};
        GridPane dayLabels = new GridPane();

        // Set column constraints for day labels to match main calendar grid
        for (int i = 0; i < 7; i++) {
            javafx.scene.layout.ColumnConstraints colConstraint = new javafx.scene.layout.ColumnConstraints();
            colConstraint.setPercentWidth(100.0 / 7.0);
            dayLabels.getColumnConstraints().add(colConstraint);
        }

        int col = 0;
        for (Text txt : dayNames) {
            AnchorPane ap = new AnchorPane();
            ap.setPrefSize(200, 40);
            txt.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
            AnchorPane.setBottomAnchor(txt, 10.0);
            AnchorPane.setLeftAnchor(txt, 10.0);
            ap.getChildren().add(txt);
            ap.setStyle("-fx-background-color: #2ECC40; -fx-border-color: #122023; -fx-border-width: 1px;"
                    + " -fx-background-radius: 5px; -fx-border-radius: 3px;");
            dayLabels.add(ap, col++, 0);
        }

        // Title bar with month navigation buttons and a back button.
        calendarTitle = new Text();
        calendarTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #FFFFFF;");

        Button previousMonth = new Button("<<");
        previousMonth.setPrefSize(80, 40);
        previousMonth.setStyle("-fx-font-size: 16px;");
        previousMonth.setOnAction(e -> previousMonth());

        Button nextMonth = new Button(">>");
        nextMonth.setPrefSize(80, 40);
        nextMonth.setStyle("-fx-font-size: 16px;");
        nextMonth.setOnAction(e -> nextMonth());

        // Back button to return to the selection pane.
        Button backButton = new Button("Back");
        backButton.setPrefSize(100, 40);
        backButton.setStyle("-fx-font-size: 16px;");
        backButton.setOnAction(e -> mainView.getChildren().setAll(selectionPane));

        // Arrange back, previous, title, and next in the title bar.
        HBox titleBar = new HBox(20, backButton, previousMonth, calendarTitle, nextMonth);
        titleBar.setAlignment(Pos.CENTER);
        titleBar.setPadding(new Insets(15));

        // Populate the calendar cells with dates.
        populateCalendar(yearMonth);

        // If in report mode, override the default cell click events.
        if (reportMode) {
            enableReportMode();
        }

        // Use VBox.setVgrow to allow the calendar to fill available space
        VBox.setVgrow(calendar, javafx.scene.layout.Priority.ALWAYS);

        // Assemble the calendar view.
        calendarViewVBox.getChildren().addAll(titleBar, dayLabels, calendar);
    }

    /**
     * Fill each calendar cell with the correct day number.
     * Dates outside the current month are styled lighter.
     *
     * @param yearMonth the month/year to render
     */
    public void populateCalendar(YearMonth yearMonth) {
        LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        // Roll back to the previous Sunday.
        while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY")) {
            calendarDate = calendarDate.minusDays(1);
        }
        for (AnchorPaneNode ap : allCalendarDays) {
            // Clear previous content.
            ap.getChildren().clear();
            Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));

            String textStyle;
            if (calendarDate.getMonth() != yearMonth.getMonth()) {
                textStyle = "-fx-fill: #AAAAAA; -fx-font-size: 20px;";
            } else {
                textStyle = "-fx-fill: #333333; -fx-font-size: 20px;";
            }

            // In selection mode, highlight selected dates
            if (selectionMode && selectedDates.contains(calendarDate)) {
                ap.setStyle("-fx-background-color: #2ECC40; -fx-border-color: #122023; -fx-border-width: 1px;"
                        + " -fx-background-radius: 5px; -fx-border-radius: 3px;");
                textStyle = "-fx-fill: #FFFFFF; -fx-font-size: 20px; -fx-font-weight: bold;";
            } else {
                ap.setStyle("-fx-background-color: #e0ffe4; -fx-border-color: #122023; -fx-border-width: 1px;"
                        + " -fx-background-radius: 5px; -fx-border-radius: 3px;");
            }

            txt.setStyle(textStyle);
            ap.setDate(calendarDate);

            // Set up click event based on mode
            if (selectionMode) {
                setupSelectionModeClickEvent(ap);
            } else if (reportMode) {
                setupReportModeClickEvent(ap);
            } else {
                setupBookingModeClickEvent(ap);
            }

            AnchorPane.setTopAnchor(txt, 10.0);
            AnchorPane.setLeftAnchor(txt, 10.0);
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

    /**
     * For report mode, override each day cell's on-click event to open the daily sheet.
     */
    private void enableReportMode() {
        for (AnchorPaneNode ap : allCalendarDays) {
            setupReportModeClickEvent(ap);
        }
    }

    /**
     * Setup click event for selection mode
     */
    private void setupSelectionModeClickEvent(AnchorPaneNode ap) {
        ap.setOnMouseClicked(e -> {
            LocalDate clickedDate = ap.getDate();

            // Toggle selection state for this date
            if (selectedDates.contains(clickedDate)) {
                selectedDates.remove(clickedDate);
                ap.setStyle("-fx-background-color: #e0ffe4; -fx-border-color: #122023; -fx-border-width: 1px;"
                        + " -fx-background-radius: 5px; -fx-border-radius: 3px;");
                // Update text style
                Text txt = (Text) ap.getChildren().get(0);
                if (clickedDate.getMonth() != currentYearMonth.getMonth()) {
                    txt.setStyle("-fx-fill: #AAAAAA; -fx-font-size: 20px;");
                } else {
                    txt.setStyle("-fx-fill: #333333; -fx-font-size: 20px;");
                }
            } else {
                // If not multi-select, clear previous selections unless Ctrl is held
                if (!multiSelectEnabled && !e.isControlDown()) {
                    clearAllSelections();
                }

                selectedDates.add(clickedDate);
                ap.setStyle("-fx-background-color: #2ECC40; -fx-border-color: #122023; -fx-border-width: 1px;"
                        + " -fx-background-radius: 5px; -fx-border-radius: 3px;");

                // Update text style
                Text txt = (Text) ap.getChildren().get(0);
                txt.setStyle("-fx-fill: #FFFFFF; -fx-font-size: 20px; -fx-font-weight: bold;");
            }

            // Trigger callback if set
            if (onDateSelectionCallback != null) {
                onDateSelectionCallback.run();
            }
        });
    }

    /**
     * Setup click event for report mode
     */
    private void setupReportModeClickEvent(AnchorPaneNode ap) {
        ap.setOnMouseClicked(e -> showDailySheet(ap));
    }

    /**
     * Setup click event for booking mode
     */
    private void setupBookingModeClickEvent(AnchorPaneNode ap) {
        ap.setOnMouseClicked(e -> ap.showBookingScreen());
    }

    private void showDailySheet(AnchorPaneNode dayNode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lancaster/ui/dailySheet.fxml"));
            Parent dailySheet = loader.load();
            // Pass the date from the clicked calendar cell to the daily sheet controller.
            DailySheetController controller = loader.getController();
            controller.setDate(dayNode.getDate());
            mainView.getChildren().setAll(dailySheet);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Clear all date selections and update UI
     */
    public void clearAllSelections() {
        selectedDates.clear();
        populateCalendar(currentYearMonth);
    }

    /**
     * Enable or disable selection mode
     */
    public void setSelectionMode(boolean selectionMode) {
        this.selectionMode = selectionMode;
        populateCalendar(currentYearMonth);
    }

    /**
     * Enable or disable multi-selection
     */
    public void setMultiSelectEnabled(boolean multiSelectEnabled) {
        this.multiSelectEnabled = multiSelectEnabled;
    }

    /**
     * Set a callback to be called when date selection changes
     */
    public void setOnDateSelectionChanged(Runnable callback) {
        this.onDateSelectionCallback = callback;
    }

    /**
     * Add a date to the selected dates set
     */
    public void addSelectedDate(LocalDate date) {
        selectedDates.add(date);
        populateCalendar(currentYearMonth);
    }

    /**
     * Get all selected dates
     */
    public Set<LocalDate> getSelectedDates() {
        return new HashSet<>(selectedDates);
    }

    /**
     * Create a compact version of this calendar for embedding
     */
    public Node createEmbeddedCalendarView() {
        // Create a mini version of the calendar by removing the back button
        VBox embeddedView = new VBox();
        embeddedView.setStyle("-fx-background-color: #122023; -fx-padding: 10px;");
        embeddedView.setFillWidth(true);

        // Title bar with month navigation buttons only
        calendarTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #FFFFFF;");

        Button previousMonth = new Button("<<");
        previousMonth.setPrefSize(60, 30);
        previousMonth.setStyle("-fx-font-size: 14px;");
        previousMonth.setOnAction(e -> previousMonth());

        Button nextMonth = new Button(">>");
        nextMonth.setPrefSize(60, 30);
        nextMonth.setStyle("-fx-font-size: 14px;");
        nextMonth.setOnAction(e -> nextMonth());

        HBox titleBar = new HBox(15, previousMonth, calendarTitle, nextMonth);
        titleBar.setAlignment(Pos.CENTER);
        titleBar.setPadding(new Insets(10));

        // Create a header row showing abbreviated day names
        Text[] dayNames = {new Text("Su"), new Text("Mo"), new Text("Tu"),
                new Text("We"), new Text("Th"), new Text("Fr"), new Text("Sa")};
        GridPane dayLabels = new GridPane();

        // Set column constraints for day labels to match main calendar grid
        for (int i = 0; i < 7; i++) {
            javafx.scene.layout.ColumnConstraints colConstraint = new javafx.scene.layout.ColumnConstraints();
            colConstraint.setPercentWidth(100.0 / 7.0);
            dayLabels.getColumnConstraints().add(colConstraint);
        }

        int col = 0;
        for (Text txt : dayNames) {
            AnchorPane ap = new AnchorPane();
            ap.setPrefSize(30, 30);
            txt.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
            AnchorPane.setBottomAnchor(txt, 8.0);
            AnchorPane.setLeftAnchor(txt, 8.0);
            ap.getChildren().add(txt);
            ap.setStyle("-fx-background-color: #2ECC40; -fx-border-color: #122023; -fx-border-width: 1px;"
                    + " -fx-background-radius: 5px; -fx-border-radius: 3px;");
            dayLabels.add(ap, col++, 0);
        }

        embeddedView.getChildren().addAll(titleBar, dayLabels, calendar);

        return embeddedView;
    }

    public StackPane getView() {
        return mainView;  // For consistency; controller swaps in calendarView.
    }

    public Node getCalendarView() {
        return calendarView;
    }

    public VBox getCalendarAsNode() {
        return (VBox) calendarView;
    }

    public YearMonth getCurrentYearMonth() {
        return currentYearMonth;
    }
}