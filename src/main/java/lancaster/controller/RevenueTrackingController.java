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
 * Controller class for managing Rev Tracking UI
 * Handles display and interaction of revenue data, charts, and filters.
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
     * Initializes the controller after FXML fields are injected.
     * Sets up UI components, default values, and initial data.
     */
    @FXML
    public void initialize() {
        // Initialize date pickers
        startDatePicker.setValue(LocalDate.now().minusMonths(1));
        endDatePicker.setValue(LocalDate.now());

        // Initialize venue filter
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

        // Configure table columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        eventColumn.setCellValueFactory(new PropertyValueFactory<>("event"));
        venueColumn.setCellValueFactory(new PropertyValueFactory<>("venue"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        revenueColumn.setCellValueFactory(new PropertyValueFactory<>("revenue"));

        setCellFactory(dateColumn);
        setCellFactory(eventColumn);
        setCellFactory(venueColumn);
        setCellFactory(durationColumn);
        setCellFactory(revenueColumn);

        revenueTable.setItems(revenueData);

        // Style the table
//        revenueTable.setStyle("-fx-text-fill: white;");
        venueRevPieChart.setStyle("-fx-background-color: #1A2C30;");
        monthlyRevBarChart.setStyle("-fx-background-color: #1A2C30;");

        // Load sample data
        loadSampleData();
        updateCharts();
    }

    /**
     * Configures a table column with a custom cell factory for consistent styling.
     *
     * @param column The table column to style.
     * @param <T>    The type of data in the column.
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
     * (Navigation implementation would depend on the application's structure.)
     */
    @FXML
    private void handleBack() {
        // Navigation back to main menu
    }

    /**
     * Applies filters based on user selections and refreshes the data display.
     */
    @FXML
    private void handleApplyFilter() {
        // Apply filters based on user selection
        String venueFilter = venueFilterDropdown.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        // In a real implementation, this would filter your data source
        // For now, we'll just reload the sample data
        loadSampleData();
        updateCharts();
    }

    /**
     * Handles the export button action to export revenue data.
     * (Export functionality would be implemented here.)
     */
    @FXML
    private void handleExport() {
        // Export functionality would go here
    }

    /**
     * Loads sample revenue data into the table and updates totals.
     * In a real application, this would fetch data from a database.
     */
    private void loadSampleData() {
        revenueData.clear();

        // sample data
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

        // calculating toalas
        double roomHireTotal = revenueData.stream().mapToDouble(RevenueData::getRevenue).sum();
        double ticketSalesTotal = 3850.00; // Placeholder until Box Office API is available
        double totalRevenue = roomHireTotal + ticketSalesTotal;

        roomHireLabel.setText(String.format("£%.2f", roomHireTotal));
        ticketSalesLabel.setText(String.format("£%.2f", ticketSalesTotal));
        totalRevenueLabel.setText(String.format("£%.2f", totalRevenue));
    }

    /**
     * Updates all charts with current data.
     */
    private void updateCharts() {
        updatePieChart();
        updateMonthlyTrendsChart();
        updateYearComparisonChart();
    }

    /**
     * Updates the pie chart with revenue distribution by venue.
     */
    private void updatePieChart() {
        venueRevPieChart.getData().clear();

        // Group revenue by venue
        Map<String, Double> revenueByVenue = new HashMap<>();
        for (RevenueData data : revenueData) {
            revenueByVenue.merge(data.getVenue(), data.getRevenue(), Double::sum);
        }

        // Add data to pie chart
        revenueByVenue.forEach((venue, total) -> {
            PieChart.Data slice = new PieChart.Data(venue, total);
            venueRevPieChart.getData().add(slice);
        });

        // Style the pie chart
        venueRevPieChart.setLegendVisible(true);
        venueRevPieChart.setLabelsVisible(true);
    }

    /**
     * Updates the monthly trends bar chart with sample data.
     */
    private void updateMonthlyTrendsChart() {
        monthlyRevBarChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Revenue");

        // Sample data
        series.getData().add(new XYChart.Data<>("Jan", 42000));
        series.getData().add(new XYChart.Data<>("Feb", 48000));
        series.getData().add(new XYChart.Data<>("Mar", 52000));
        series.getData().add(new XYChart.Data<>("Apr", 48700));

        monthlyRevBarChart.getData().add(series);

        // Style the chart
        monthlyRevBarChart.setLegendVisible(true);
    }

    /**
     * Updates the year comparison bar chart with sample data for two years.
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

        // Style chart
        yearRevComparisonChart.setLegendVisible(true);
    }

    /**
     * Class to represent a single revenue data entry.
     */
    public static class RevenueData {
        private String date;
        private String event;
        private String venue;
        private int duration;
        private double revenue;

        /**
         * Construct new revenue data entry.
         *
         * @param date     The event date.
         * @param event    The event name.
         * @param venue    The venue name.
         * @param duration The event duration in hours.
         * @param revenue  The revenue amount.
         */
        public RevenueData(String date, String event, String venue, int duration, double revenue) {
            this.date = date;
            this.event = event;
            this.venue = venue;
            this.duration = duration;
            this.revenue = revenue;
        }

        // Getters for accessing revenue data properties
        public String getDate() { return date; }
        public String getEvent() { return event; }
        public String getVenue() { return venue; }
        public int getDuration() { return duration; }
        public double getRevenue() { return revenue; }
    }
}
