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

/**
 * Represents a full calendar view UI component.
 * <p>
 * This class constructs a calendar view for a given month and year along with navigation and selection controls.
 * It supports different modes, such as report mode (to view daily sheets), selection mode (to select one or more dates),
 * and default booking mode (to open booking type selection UI on a day cell).
 * The calendar is displayed using a grid of {@link AnchorPaneNode} instances along with headers and title bars.
 * </p>
 */
public class FullCalendarView {

    private ArrayList<AnchorPaneNode> allCalendarDays = new ArrayList<>(35);
    private StackPane mainView;    // The main container for swapping views.
    private Text calendarTitle;
    private YearMonth currentYearMonth;
    private Node calendarView;     // The calendar view node (a VBox in this case).
    private Node selectionPane;    // The original selection pane to return to.
    private GridPane calendar;     // The grid used to display the calendar cells.

    // Calendar modes and selection management.
    private boolean reportMode = false;
    private boolean selectionMode = false;
    private Set<LocalDate> selectedDates = new HashSet<>();
    private boolean multiSelectEnabled = false;

    // Callback for when the date selection changes.
    private Runnable onDateSelectionCallback = null;

    /**
     * Constructs a full calendar view in booking mode for the specified month and year.
     *
     * @param yearMonth     the month and year to be displayed.
     * @param mainView      the main {@link StackPane} container used for swapping views.
     * @param selectionPane the original selection pane to return to.
     */
    public FullCalendarView(YearMonth yearMonth, StackPane mainView, Node selectionPane) {
        this(yearMonth, mainView, selectionPane, false);
    }

    /**
     * Constructs a full calendar view for the specified month and year.
     * <p>
     * If {@code reportMode} is true, clicking on a day cell opens the daily sheet for that day; otherwise,
     * it opens the booking type selection screen.
     * </p>
     *
     * @param yearMonth     the month and year to display.
     * @param mainView      the main container (a {@link StackPane}) for swapping views.
     * @param selectionPane the original selection pane to return to.
     * @param reportMode    if {@code true}, clicking on days will open the daily sheet; if {@code false}, the booking screen is shown.
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
        // Allow the calendar to grow/shrink with the window.
        calendar.setVgrow(calendar, javafx.scene.layout.Priority.ALWAYS);
        calendar.setHgrow(calendar, javafx.scene.layout.Priority.ALWAYS);
        calendar.setGridLinesVisible(true);

        // Set column constraints to make columns equally sized.
        for (int i = 0; i < 7; i++) {
            javafx.scene.layout.ColumnConstraints colConstraint = new javafx.scene.layout.ColumnConstraints();
            colConstraint.setPercentWidth(100.0 / 7.0);
            calendar.getColumnConstraints().add(colConstraint);
        }

        // Set row constraints for 5 equal rows.
        for (int i = 0; i < 5; i++) {
            javafx.scene.layout.RowConstraints rowConstraint = new javafx.scene.layout.RowConstraints();
            rowConstraint.setPercentHeight(100.0 / 5.0);
            calendar.getRowConstraints().add(rowConstraint);
        }

        // Create 35 cells (5 rows x 7 columns) for the calendar.
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                // Create a calendar cell with access to mainView and calendarView for navigation.
                AnchorPaneNode ap = new AnchorPaneNode(mainView, calendarView);
                // Let the cell expand with the grid.
                ap.setPrefSize(200, 150);
                ap.setStyle("-fx-background-color: #f8f8f8; -fx-border-color: #122023; -fx-border-width: 1px;"
                        + " -fx-background-radius: 5px; -fx-border-radius: 3px;");
                calendar.add(ap, j, i);
                allCalendarDays.add(ap);
            }
        }

        // Create a header row showing the day names.
        Text[] dayNames = { new Text("Sunday"), new Text("Monday"), new Text("Tuesday"),
                new Text("Wednesday"), new Text("Thursday"), new Text("Friday"), new Text("Saturday") };
        GridPane dayLabels = new GridPane();

        // Set column constraints for day labels to match the main calendar grid.
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

        // Create a title bar with month navigation buttons and a back button.
        calendarTitle = new Text();
        calendarTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #f8f8f8;");

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

        previousMonth.setStyle("-fx-background-color: #2ECC4026; -fx-text-fill: #2ECC40; -fx-font-size: 20px;");
        nextMonth.setStyle("-fx-background-color: #2ECC4026; -fx-text-fill: #2ECC40; -fx-font-size: 20px;");
        backButton.setStyle("-fx-background-color: #2ECC4026; -fx-text-fill: #2ECC40; -fx-font-size: 20px;");

        // Arrange navigation buttons and title in a horizontal box.
        HBox titleBar = new HBox(20, backButton, previousMonth, calendarTitle, nextMonth);
        titleBar.setAlignment(Pos.CENTER);
        titleBar.setPadding(new Insets(15));

        // Populate the calendar cells with dates.
        populateCalendar(currentYearMonth);

        // If in report mode, override the default cell click events.
        if (reportMode) {
            enableReportMode();
        }

        // Allow the calendar to fill available vertical space.
        VBox.setVgrow(calendar, javafx.scene.layout.Priority.ALWAYS);

        // Assemble the calendar view.
        calendarViewVBox.getChildren().addAll(titleBar, dayLabels, calendar);
    }

    /**
     * Populates the calendar cells with the correct day numbers.
     * <p>
     * The calendar starts with the first Sunday before or on the first day of the specified month.
     * Cells corresponding to dates outside the current month are styled with a lighter color.
     * In selection mode, selected dates are highlighted.
     * </p>
     *
     * @param yearMonth the month and year for which to render the calendar.
     */
    public void populateCalendar(YearMonth yearMonth) {
        LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY")) {
            calendarDate = calendarDate.minusDays(1);
        }
        for (AnchorPaneNode ap : allCalendarDays) {
            ap.getChildren().clear();
            Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));

            String textStyle;
            if (calendarDate.getMonth() != yearMonth.getMonth()) {
                textStyle = "-fx-font-size: 24px; -fx-fill: grey;";
            } else {
                textStyle = "-fx-font-size: 24px;";
            }
            if (selectionMode && selectedDates.contains(calendarDate)) {
                ap.setStyle("-fx-background-color: #e0ffe4; -fx-background-radius: 5px;");
                textStyle = "-fx-font-size: 24px; -fx-font-weight: bold;";
            } else {
                ap.setStyle("-fx-background-color: #f8f8f8; -fx-background-radius: 5px;");
            }

            txt.setStyle(textStyle);
            ap.setOnMouseEntered(e -> {
                if (selectionMode && selectedDates.contains(ap.getDate())) {
                    ap.setStyle("-fx-background-color: #e0ffe4; -fx-background-radius: 5px;");
                } else {
                    ap.setStyle("-fx-background-color: #f8f8f8; -fx-background-radius: 5px;");
                }
            });
            ap.setOnMouseExited(e -> {
                if (selectionMode && selectedDates.contains(ap.getDate())) {
                    ap.setStyle("-fx-background-color: #e0ffe4; -fx-background-radius: 5px;");
                } else {
                    ap.setStyle("-fx-background-color: #f8f8f8; -fx-background-radius: 5px;");
                }
            });

            StackPane sp = new StackPane(txt);
            sp.setAlignment(Pos.CENTER);
            AnchorPane.setTopAnchor(sp, 0.0);
            AnchorPane.setBottomAnchor(sp, 0.0);
            AnchorPane.setLeftAnchor(sp, 0.0);
            AnchorPane.setRightAnchor(sp, 0.0);

            ap.setDate(calendarDate);

            if (selectionMode) {
                setupSelectionModeClickEvent(ap);
            } else if (reportMode) {
                setupReportModeClickEvent(ap);
            } else {
                setupBookingModeClickEvent(ap);
            }

            ap.getChildren().add(sp);
            calendarDate = calendarDate.plusDays(1);
        }
        calendarTitle.setText(yearMonth.getMonth().toString() + " " + yearMonth.getYear());
    }

    /**
     * Switches the calendar view to the previous month.
     */
    private void previousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        populateCalendar(currentYearMonth);
    }

    /**
     * Switches the calendar view to the next month.
     */
    private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        populateCalendar(currentYearMonth);
    }

    /**
     * Enables report mode by overriding the default click events for each day cell.
     * <p>
     * In report mode, clicking on a day cell opens the daily sheet view.
     * </p>
     */
    private void enableReportMode() {
        for (AnchorPaneNode ap : allCalendarDays) {
            setupReportModeClickEvent(ap);
        }
    }

    /**
     * Sets up the click event for a calendar cell when in selection mode.
     * <p>
     * In selection mode, clicking a cell toggles its selection state and applies the appropriate styling.
     * If multi-select is disabled and the Control key is not held, previous selections are cleared.
     * </p>
     *
     * @param ap the {@link AnchorPaneNode} representing the calendar cell.
     */
    private void setupSelectionModeClickEvent(AnchorPaneNode ap) {
        ap.setOnMouseClicked(e -> {
            LocalDate clickedDate = ap.getDate();

            StackPane sp = (StackPane) ap.getChildren().get(0);
            Text txt = (Text) sp.getChildren().get(0);

            if (selectedDates.contains(clickedDate)) {
                selectedDates.remove(clickedDate);
                ap.setStyle("-fx-background-color: #f8f8f8; -fx-background-radius: 5px;");
                txt.setStyle("-fx-font-size: 24px;");
            } else {
                if (!multiSelectEnabled && !e.isControlDown()) {
                    clearAllSelections();
                }
                selectedDates.add(clickedDate);
                ap.setStyle("-fx-background-color: #e0ffe4; -fx-background-radius: 5px;");
                txt.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
            }

            if (onDateSelectionCallback != null) {
                onDateSelectionCallback.run();
            }
        });
    }

    /**
     * Sets up the click event for a calendar cell when in report mode.
     * <p>
     * In report mode, clicking on a cell opens the daily sheet view for the corresponding date.
     * </p>
     *
     * @param ap the {@link AnchorPaneNode} representing the calendar cell.
     */
    private void setupReportModeClickEvent(AnchorPaneNode ap) {
        ap.setOnMouseClicked(e -> showDailySheet(ap));
    }

    /**
     * Sets up the click event for a calendar cell in booking mode.
     * <p>
     * In booking mode, clicking on a cell displays the booking type selection UI.
     * </p>
     *
     * @param ap the {@link AnchorPaneNode} representing the calendar cell.
     */
    private void setupBookingModeClickEvent(AnchorPaneNode ap) {
        ap.setOnMouseClicked(e -> ap.showBookingTypeSelection());
    }

    /**
     * Opens the daily sheet view for the specified calendar cell.
     * <p>
     * This method loads the daily sheet FXML, sets the date in the controller, and displays it in the main view.
     * </p>
     *
     * @param dayNode the calendar cell whose date will be used.
     */
    private void showDailySheet(AnchorPaneNode dayNode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lancaster/ui/dailySheet.fxml"));
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
     * Clears all date selections and updates the calendar UI.
     */
    public void clearAllSelections() {
        selectedDates.clear();
        populateCalendar(currentYearMonth);
    }

    /**
     * Enables or disables selection mode for the calendar.
     *
     * @param selectionMode {@code true} to enable selection mode; {@code false} to disable it.
     */
    public void setSelectionMode(boolean selectionMode) {
        this.selectionMode = selectionMode;
        populateCalendar(currentYearMonth);
    }

    /**
     * Enables or disables multi-selection of dates.
     *
     * @param multiSelectEnabled {@code true} to allow multiple date selections; {@code false} otherwise.
     */
    public void setMultiSelectEnabled(boolean multiSelectEnabled) {
        this.multiSelectEnabled = multiSelectEnabled;
    }

    /**
     * Sets a callback to be executed whenever the date selection changes.
     *
     * @param callback a {@link Runnable} to run on date selection change.
     */
    public void setOnDateSelectionChanged(Runnable callback) {
        this.onDateSelectionCallback = callback;
    }

    /**
     * Adds a date to the set of selected dates and refreshes the calendar view.
     *
     * @param date the date to add.
     */
    public void addSelectedDate(LocalDate date) {
        selectedDates.add(date);
        populateCalendar(currentYearMonth);
    }

    /**
     * Retrieves the set of currently selected dates.
     *
     * @return a {@link Set} of selected {@link LocalDate} objects.
     */
    public Set<LocalDate> getSelectedDates() {
        return new HashSet<>(selectedDates);
    }

    /**
     * Creates a compact, embedded version of the calendar view.
     * <p>
     * This method constructs a mini version of the calendar by removing the back button
     * and using abbreviated day names.
     * </p>
     *
     * @return a {@link Node} representing the embedded calendar view.
     */
    public Node createEmbeddedCalendarView() {
        // Create a mini version of the calendar by removing the back button.
        VBox embeddedView = new VBox();
        embeddedView.setStyle("-fx-background-color: #122023; -fx-padding: 10px;");
        embeddedView.setFillWidth(true);

        // Title bar with month navigation buttons only.
        calendarTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: black;");

        Button previousMonth = new Button("<<");
        previousMonth.setPrefSize(60, 30);
        previousMonth.setStyle("-fx-font-size: 14px;");
        previousMonth.setOnAction(e -> previousMonth());

        Button nextMonth = new Button(">>");
        nextMonth.setPrefSize(60, 30);
        nextMonth.setStyle("-fx-font-size: 14px;");
        nextMonth.setOnAction(e -> nextMonth());
        previousMonth.setStyle("-fx-background-color: #2ECC4026; -fx-text-fill: #2ECC40; -fx-font-size: 20px;");
        nextMonth.setStyle("-fx-background-color: #2ECC4026; -fx-text-fill: #2ECC40; -fx-font-size: 20px;");
        HBox titleBar = new HBox(15, previousMonth, calendarTitle, nextMonth);
        titleBar.setAlignment(Pos.CENTER);
        titleBar.setPadding(new Insets(10));

        // Create a header row showing abbreviated day names.
        Text[] dayNames = { new Text("Sun"), new Text("Mon"), new Text("Tue"),
                new Text("Wed"), new Text("Thu"), new Text("Fri"), new Text("Sat") };
        GridPane dayLabels = new GridPane();

        // Set column constraints for day labels to match main calendar grid.
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

    /**
     * Returns the main view of the calendar.
     *
     * @return the {@link StackPane} containing the calendar.
     */
    public StackPane getView() {
        return mainView;
    }

    /**
     * Returns the node representing the calendar view.
     *
     * @return the calendar view as a {@link Node}.
     */
    public Node getCalendarView() {
        return calendarView;
    }

    /**
     * Returns the calendar view casted as a {@link VBox}.
     *
     * @return the calendar view as a {@link VBox}.
     */
    public VBox getCalendarAsNode() {
        return (VBox) calendarView;
    }

    /**
     * Returns the currently displayed year and month.
     *
     * @return the current {@link YearMonth} of the calendar.
     */
    public YearMonth getCurrentYearMonth() {
        return currentYearMonth;
    }
}
