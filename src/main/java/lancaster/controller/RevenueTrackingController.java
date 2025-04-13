package lancaster.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lancaster.model.RevenueCalculator;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller class for managing the Revenue Tracking user interface.
 * <p>
 * This class is responsible for initializing and updating the UI components involved in revenue tracking,
 * including labels, charts, filters, and tables. It displays revenue data such as total revenue, room hire,
 * and ticket sales, and updates various charts (pie chart, bar charts) based on sample data or applied filters.
 * </p>
 */
public class RevenueTrackingController {

    @FXML private Label totalRevenueLabel;
    @FXML private Label roomHireLabel;
    @FXML private Label ticketSalesLabel;

    @FXML private PieChart venueRevPieChart;
    @FXML private BarChart<String, Number> monthlyRevBarChart;
    @FXML private BarChart<String, Number> yearRevComparisonChart;

    @FXML private ComboBox<String> venueFilterDropdown;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;

    @FXML private TableView<RevenueData> revenueTable;
    @FXML private TableColumn<RevenueData, String> dateColumn;
    @FXML private TableColumn<RevenueData, String> eventColumn;
    @FXML private TableColumn<RevenueData, String> venueColumn;
    @FXML private TableColumn<RevenueData, Integer> durationColumn;
    @FXML private TableColumn<RevenueData, Double> revenueColumn;

    private RevenueCalculator revenueCalculator = new RevenueCalculator();
    private ObservableList<RevenueData> revenueData = FXCollections.observableArrayList();

    /**
     * Initializes the Revenue Tracking controller after FXML fields are injected.
     * <p>
     * This method sets the default values for date pickers, initializes the venue filter dropdown with available options,
     * configures the table columns with custom cell factories, applies initial styling to charts,
     * and loads sample revenue data while updating the charts accordingly.
     * </p>
     */
    @FXML
    public void initialize() {
        // Initialize date pickers with default date ranges.
        startDatePicker.setValue(LocalDate.now().minusMonths(1));
        endDatePicker.setValue(LocalDate.now());

        // Initialize venue filter dropdown with available venues.
        venueFilterDropdown.getItems().addAll(
                "All Venues",
                "Main Hall",
                "Small Hall",
                "Whole Venue",
                "Rehearsal Space",
                "Green Room",
                "Brontë Boardroom",
                "Dickens Den",
                "Poe Parlor",
                "Globe Room",
                "Chekhov Chamber"
        );
        venueFilterDropdown.setValue("All Venues");

        // Configure table columns to bind to the properties of RevenueData.
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        eventColumn.setCellValueFactory(new PropertyValueFactory<>("event"));
        venueColumn.setCellValueFactory(new PropertyValueFactory<>("venue"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        revenueColumn.setCellValueFactory(new PropertyValueFactory<>("revenue"));

        // Apply custom cell styling to all columns.
        setCellFactory(dateColumn);
        setCellFactory(eventColumn);
        setCellFactory(venueColumn);
        setCellFactory(durationColumn);
        setCellFactory(revenueColumn);

        revenueTable.setItems(revenueData);

        // Apply styling to charts.
        // revenueTable.setStyle("-fx-text-fill: white;");
        venueRevPieChart.setStyle("-fx-background-color: #1A2C30;");
        monthlyRevBarChart.setStyle("-fx-background-color: #1A2C30;");

        // Load sample revenue data and update the associated charts.
        loadSampleData();
        updateCharts();
    }

    /**
     * Configures a given table column with a custom cell factory for consistent styling.
     * <p>
     * This method sets up a cell factory that ensures each cell in the column uses white text on a background color.
     * This enhances the visual consistency of the revenue table.
     * </p>
     *
     * @param column the table column to configure.
     * @param <T>    the type of data contained within the column.
     */
    private <T> void setCellFactory(TableColumn<RevenueData, T> column) {
        column.setCellFactory(col -> new TableCell<RevenueData, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.toString());
                    setTextFill(javafx.scene.paint.Color.WHITE);
                    setStyle("-fx-background-color: #1A2C30;");
                }
            }
        });
    }

    /**
     * Handles the back button action to return to the main menu.
     */
    @FXML
    private void handleBack() {
        // Navigation back to main menu (implementation dependent on application structure).
    }

    /**
     * Applies user-selected filters and refreshes the revenue data display.
     * <p>
     * Retrieves filter values (venue selection and date range), and applies these filters.
     * It reloads the data and updates the charts.
     * </p>
     */
    @FXML
    private void handleApplyFilter() {
        String venueFilter = venueFilterDropdown.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        loadSampleData();
        updateCharts();
    }

    /**
     * Handles the export button action to export revenue data.
     */
    @FXML
    private void handleExport() {
        // Export functionality would be implemented here.
    }

    /**
     * Loads sample revenue data into the revenue table and updates revenue totals.
     * <p>
     * This method clears any existing data, adds revenue entries to the table,
     * calculates total room hire revenue, ticket sales, and overall revenue,
     * and then updates their corresponding labels.
     * </p>
     */
    private void loadSampleData() {
        revenueData.clear();

        // Sample revenue data entries.
        revenueData.add(new RevenueData(
                "2025-04-10",
                "Friday Night Concert",
                "Main Hall",
                6,
                2220.00
        ));

        revenueData.add(new RevenueData(
                "2025-04-12",
                "Corporate Meeting",
                "Small Hall",
                4,
                270.00
        ));

        revenueData.add(new RevenueData(
                "2025-04-15",
                "Wedding Reception",
                "Globe Room",
                8,
                300.00
        ));

        // Calculate revenue totals.
        double roomHireTotal = revenueData.stream().mapToDouble(RevenueData::getRevenue).sum();
        double ticketSalesTotal = 3850.00; //
        double totalRevenue = roomHireTotal + ticketSalesTotal;

        roomHireLabel.setText(String.format("£%.2f", roomHireTotal));
        ticketSalesLabel.setText(String.format("£%.2f", ticketSalesTotal));
        totalRevenueLabel.setText(String.format("£%.2f", totalRevenue));
    }

    /**
     * Updates all revenue-related charts with the current data.
     * <p>
     * This includes updating the pie chart for venue revenue distribution,
     * the monthly revenue trends bar chart, and the yearly revenue comparison chart.
     * </p>
     */
    private void updateCharts() {
        updatePieChart();
        updateMonthlyTrendsChart();
        updateYearComparisonChart();
    }

    /**
     * Updates the pie chart to display revenue distribution by venue.
     * <p>
     * This method groups revenue entries by venue, sums up the revenue for each venue, and adds the resulting data as slices
     * to the pie chart. It also configures the visibility of the legend and labels.
     * </p>
     */
    private void updatePieChart() {
        venueRevPieChart.getData().clear();

        // Group revenue by venue.
        Map<String, Double> revenueByVenue = new HashMap<>();
        for (RevenueData data : revenueData) {
            revenueByVenue.merge(data.getVenue(), data.getRevenue(), Double::sum);
        }

        // Add grouped data as slices to the pie chart.
        revenueByVenue.forEach((venue, total) -> {
            PieChart.Data slice = new PieChart.Data(venue, total);
            venueRevPieChart.getData().add(slice);
        });

        // Configure pie chart styling.
        venueRevPieChart.setLegendVisible(true);
        venueRevPieChart.setLabelsVisible(true);
    }

    /**
     * Updates the monthly revenue trends bar chart with sample data.
     * <p>
     * This method creates a data series representing monthly revenue figures,
     * populates it with sample data, and adds it to the monthly revenue bar chart.
     * </p>
     */
    private void updateMonthlyTrendsChart() {
        monthlyRevBarChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Revenue");

        // Sample data representing monthly revenue.
        series.getData().add(new XYChart.Data<>("Jan", 42000));
        series.getData().add(new XYChart.Data<>("Feb", 48000));
        series.getData().add(new XYChart.Data<>("Mar", 52000));
        series.getData().add(new XYChart.Data<>("Apr", 48700));

        monthlyRevBarChart.getData().add(series);

        // Enable legend for the chart.
        monthlyRevBarChart.setLegendVisible(true);
    }

    /**
     * Updates the year comparison bar chart with sample data for two years.
     * <p>
     * This method creates two data series to represent revenue for the current year and the previous year,
     * populates each series with quarterly revenue figures, and adds them to the comparison chart.
     * </p>
     */
    private void updateYearComparisonChart() {
        yearRevComparisonChart.getData().clear();

        XYChart.Series<String, Number> currentYear = new XYChart.Series<>();
        currentYear.setName("2025");
        currentYear.getData().add(new XYChart.Data<>("Q1", 42000));
        currentYear.getData().add(new XYChart.Data<>("Q2", 48000));
        currentYear.getData().add(new XYChart.Data<>("Q3", 52000));
        currentYear.getData().add(new XYChart.Data<>("Q4", 48700));

        XYChart.Series<String, Number> previousYear = new XYChart.Series<>();
        previousYear.setName("2024");
        previousYear.getData().add(new XYChart.Data<>("Q1", 38000));
        previousYear.getData().add(new XYChart.Data<>("Q2", 42000));
        previousYear.getData().add(new XYChart.Data<>("Q3", 45000));
        previousYear.getData().add(new XYChart.Data<>("Q4", 41000));

        yearRevComparisonChart.getData().addAll(currentYear, previousYear);

        // Enable legend for the comparison chart.
        yearRevComparisonChart.setLegendVisible(true);
    }

    /**
     * Class representing a single revenue data entry.
     * <p>
     * Instances of this class encapsulate the details of a revenue record,
     * including the date, event name, venue, duration (in hours), and revenue amount.
     * </p>
     */
    public static class RevenueData {
        private String date;
        private String event;
        private String venue;
        private int duration;
        private double revenue;

        /**
         * Constructs a new RevenueData instance.
         *
         * @param date     the event date as a String (e.g., "2025-04-10").
         * @param event    the event name.
         * @param venue    the name of the venue where the event took place.
         * @param duration the duration of the event in hours.
         * @param revenue  the revenue generated by the event.
         */
        public RevenueData(String date, String event, String venue, int duration, double revenue) {
            this.date = date;
            this.event = event;
            this.venue = venue;
            this.duration = duration;
            this.revenue = revenue;
        }

        // Getters for accessing revenue data properties.
        public String getDate() { return date; }
        public String getEvent() { return event; }
        public String getVenue() { return venue; }
        public int getDuration() { return duration; }
        public double getRevenue() { return revenue; }
    }
}
