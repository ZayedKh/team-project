package lancaster.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.beans.property.SimpleDoubleProperty;
import lancaster.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RevenueTrackingUI extends BorderPane {

    private RevenueManager dataManager;
    private VenueSpace venueSpace;
    private BookingType bookingType;

    private DatePicker fromDatePicker;
    private DatePicker toDatePicker;
    private ComboBox<String> venueSelector;
    private Label totalRevenueLabel;
    private Label roomHireLabel;
    private Label ticketSalesLabel;

    private TableView<RevenueEntry> revenueTable;
    private PieChart revenuePieChart;
    private BarChart<String, Number> revenueBarChart;
    private BarChart<String, Number> yearComparisonChart;

    // Custom color scheme
    private static final Color TEXT_COLOR = Color.BLACK;
    private static final String[] PIE_COLORS = {"#2ECC40", "#005355", "#00a561", "#1a2c30", "#4472C4"};
    private static final String[] BAR_COLORS = {"#2ECC40", "#005355"};

    // Consistent text styling
    private static final String AXIS_STYLE = "-fx-tick-label-fill: black; -fx-font-family: 'Cambria';";
    private static final String LABEL_STYLE = "-fx-text-fill: black; -fx-font-family: 'Cambria';";
    private static final String WHITE_BACKGROUND = "-fx-background-color: white;";

    /**
     * Constructs the revenue tracking dashboard, initializing the data manager and UI components.
     */
    public RevenueTrackingUI() {
        dataManager = new RevenueManager();

        setupUI();

        Platform.runLater(this::updateCharts);
    }

    /**
     * Sets up the dashboard layout with a filter bar at the top and a tabbed interface in the center.
     */
    private void setupUI() {
        this.setPadding(new Insets(10));
        this.setStyle(WHITE_BACKGROUND);

        HBox filterBar = createFilterBar();
        this.setTop(filterBar);

        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-text-fill: black;");

        Tab overviewTab = new Tab("Revenue Overview");
        Tab detailedTab = new Tab("Detailed Reports");
        Tab comparisonTab = new Tab("Year Comparison");
        Tab calculationTab = new Tab("Calculate Revenue");

        overviewTab.setClosable(false);
        detailedTab.setClosable(false);
        comparisonTab.setClosable(false);
        calculationTab.setClosable(false);

        // Setting up the content for each tab
        overviewTab.setContent(createOverviewContent());
        detailedTab.setContent(createDetailedContent());
        comparisonTab.setContent(createComparisonContent());
        calculationTab.setContent(createCalculationContent());

        tabPane.getTabs().addAll(overviewTab, detailedTab, comparisonTab, calculationTab);

        this.setCenter(tabPane);
    }
    /**
     * Creates a filter bar with date pickers, venue dropdown, and action buttons.
     *
     * @return an HBox containing the filter controls
     */
    private HBox createFilterBar() {
        HBox filterBar = new HBox(10);
        filterBar.setPadding(new Insets(10));
        filterBar.setStyle(WHITE_BACKGROUND);

        Label fromLabel = new Label("From:");
        fromLabel.setTextFill(TEXT_COLOR);
        fromLabel.setStyle(LABEL_STYLE);

        fromDatePicker = new DatePicker(LocalDate.now().minusMonths(1));

        Label toLabel = new Label("To:");
        toLabel.setTextFill(TEXT_COLOR);
        toLabel.setStyle(LABEL_STYLE);

        toDatePicker = new DatePicker(LocalDate.now());

        Label venueLabel = new Label("Venue:");
        venueLabel.setTextFill(TEXT_COLOR);
        venueLabel.setStyle(LABEL_STYLE);

        venueSelector = new ComboBox<>();
        venueSelector.getItems().addAll("All Venues", "Main Hall", "Small Hall", "Rehearsal Space", "Rooms");
        venueSelector.setValue("All Venues");

        Button applyFilterButton = new Button("Apply Filter");
        applyFilterButton.setOnAction(e -> updateCharts());

        // Add a refresh button that would trigger API refresh in the future
        Button refreshDataButton = new Button("Refresh Data");
        refreshDataButton.setOnAction(e -> {
            // This would call the API integration method in the future
            dataManager.loadTicketSalesData();
            updateCharts();
        });

        filterBar.getChildren().addAll(fromLabel, fromDatePicker, toLabel, toDatePicker,
                venueLabel, venueSelector, applyFilterButton, refreshDataButton);
        return filterBar;
    }

    /**
     * Creates the "Revenue Overview" tab with summary stats and visual charts.
     *
     * @return a VBox with overview content
     */
    private VBox createOverviewContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(10));
        content.setStyle(WHITE_BACKGROUND);

        GridPane summaryGrid = new GridPane();
        summaryGrid.setHgap(20);
        summaryGrid.setVgap(10);
        summaryGrid.setPadding(new Insets(10));
        summaryGrid.setStyle(WHITE_BACKGROUND);

        // Create summary labels with consistent styling
        totalRevenueLabel = new Label("£0.00");
        roomHireLabel = new Label("£0.00");
        ticketSalesLabel = new Label("£0.00");

        addStyledLabel(summaryGrid, "Total Revenue:", totalRevenueLabel, 0);
        addStyledLabel(summaryGrid, "Room Hire Revenue:", roomHireLabel, 1);
        addStyledLabel(summaryGrid, "Ticket Sales Revenue:", ticketSalesLabel, 2);

        // Set up the pie chart
        revenuePieChart = new PieChart();
        revenuePieChart.setTitle("Revenue Breakdown by Venue");
        revenuePieChart.setLegendVisible(true);
        revenuePieChart.setLabelsVisible(true);
        revenuePieChart.setStyle(WHITE_BACKGROUND);

        // Set up the bar chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        revenueBarChart = new BarChart<>(xAxis, yAxis);
        revenueBarChart.setTitle("Revenue by Type");
        revenueBarChart.setStyle(WHITE_BACKGROUND);

        xAxis.setLabel("Venue");
        xAxis.setTickLabelRotation(0);
        yAxis.setLabel("Revenue (£)");

        content.getChildren().addAll(summaryGrid, revenuePieChart, revenueBarChart);
        return content;
    }

    /**
     * Adds a styled label-value pair to a grid for displaying revenue summaries.
     *
     * @param grid       the grid to add to
     * @param labelText  the label text (e.g., "Total Revenue:")
     * @param valueLabel the label showing the value
     * @param row        the row index in the grid
     */
    private void addStyledLabel(GridPane grid, String labelText, Label valueLabel, int row) {
        Label label = new Label(labelText);
        label.setTextFill(TEXT_COLOR);
        label.setStyle(LABEL_STYLE);

        valueLabel.setTextFill(TEXT_COLOR);
        valueLabel.setStyle(LABEL_STYLE);

        grid.add(label, 0, row);
        grid.add(valueLabel, 1, row);
    }

    /**
     * Creates the "Detailed Reports" tab with a table of all revenue entries.
     *
     * @return a VBox with the detailed content
     */
    private VBox createDetailedContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(10));
        content.setStyle(WHITE_BACKGROUND);

        revenueTable = new TableView<>();
        revenueTable.setStyle("-fx-text-fill: black;");

        TableColumn<RevenueEntry, String> venueCol = new TableColumn<>("Venue");
        venueCol.setCellValueFactory(new PropertyValueFactory<>("venue"));
        venueCol.setPrefWidth(120);

        TableColumn<RevenueEntry, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setPrefWidth(100);

        TableColumn<RevenueEntry, String> bookingTypeCol = new TableColumn<>("Booking Type");
        bookingTypeCol.setCellValueFactory(new PropertyValueFactory<>("bookingType"));
        bookingTypeCol.setPrefWidth(120);

        TableColumn<RevenueEntry, Double> roomRateCol = new TableColumn<>("Room Rate (£)");
        roomRateCol.setCellValueFactory(new PropertyValueFactory<>("roomRate"));
        roomRateCol.setPrefWidth(120);

        TableColumn<RevenueEntry, Double> ticketSalesCol = new TableColumn<>("Ticket Sales (£)");
        ticketSalesCol.setCellValueFactory(new PropertyValueFactory<>("ticketSales"));
        ticketSalesCol.setPrefWidth(120);

        TableColumn<RevenueEntry, Double> totalCol = new TableColumn<>("Total Revenue (£)");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalRevenue"));
        totalCol.setPrefWidth(120);

        TableColumn<RevenueEntry, Double> clientPayableCol = new TableColumn<>("Client Payable (£)");
        clientPayableCol.setCellValueFactory(cellData -> {
            // Calculate 65% of total revenue as an example
            double totalRev = cellData.getValue().getTotalRevenue();
            double clientPayable = totalRev * 0.65; // 65% to client
            return new SimpleDoubleProperty(clientPayable).asObject();
        });
        clientPayableCol.setPrefWidth(120);

        revenueTable.setPrefHeight(500);
        revenueTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        revenueTable.getColumns().addAll(venueCol, dateCol, bookingTypeCol, roomRateCol, ticketSalesCol, totalCol, clientPayableCol);

        content.getChildren().add(revenueTable);
        return content;
    }

    /**
     * Creates the "Year Comparison" tab with a bar chart for yearly revenue trends.
     *
     * @return a VBox with the comparison content
     */
    private VBox createComparisonContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(10));
        content.setAlignment(Pos.CENTER);
        content.setStyle(WHITE_BACKGROUND);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yearComparisonChart = new BarChart<>(xAxis, yAxis);
        yearComparisonChart.setStyle(WHITE_BACKGROUND);

        yearComparisonChart.setTitle("Year-over-Year Revenue Comparison");
        xAxis.setLabel("Month");
        yAxis.setLabel("Revenue (£)");

        yearComparisonChart.setPrefHeight(500);

        styleAxis(xAxis);
        styleAxis(yAxis);

        content.getChildren().add(yearComparisonChart);
        return content;
    }

    /**
     * Creates the "Calculate Revenue" tab with a form to estimate booking revenue.
     *
     * @return a VBox with the calculation content
     */
    private VBox createCalculationContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(10));
        content.setStyle(WHITE_BACKGROUND);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.setStyle(WHITE_BACKGROUND);

        // Create all labels with black text
        Label venueLabel = new Label("Venue Space:");
        venueLabel.setTextFill(TEXT_COLOR);
        venueLabel.setStyle(LABEL_STYLE);

        ComboBox<String> venueCombo = new ComboBox<>();
        venueCombo.getItems().addAll(
                "WHOLE_VENUE", "MAIN_HALL", "SMALL_HALL", "REHEARSAL_SPACE",
                "GREEN_ROOM", "BRONTE_BOARDROOM", "DICKENS_DEN",
                "POE_PARLOR", "GLOBE_ROOM", "CHEKHOV_CHAMBER"
        );
        venueCombo.setValue("MAIN_HALL");

        Label dayLabel = new Label("Day Type:");
        dayLabel.setTextFill(TEXT_COLOR);
        dayLabel.setStyle(LABEL_STYLE);

        ComboBox<String> dayCombo = new ComboBox<>();
        dayCombo.getItems().addAll("MONDAY_TO_THURSDAY", "FRIDAY_TO_SATURDAY", "SUNDAY");
        dayCombo.setValue("MONDAY_TO_THURSDAY");

        Label bookingTypeLabel = new Label("Booking Type:");
        bookingTypeLabel.setTextFill(TEXT_COLOR);
        bookingTypeLabel.setStyle(LABEL_STYLE);

        ComboBox<String> bookingTypeCombo = new ComboBox<>();
        bookingTypeCombo.getItems().addAll("HOURLY", "EVENING", "FULL_DAY", "MORNING_AFTERNOON", "WEEKLY");
        bookingTypeCombo.setValue("FULL_DAY");

        Label hoursLabel = new Label("Hours (if applicable):");
        hoursLabel.setTextFill(TEXT_COLOR);
        hoursLabel.setStyle(LABEL_STYLE);

        ComboBox<Integer> hoursCombo = new ComboBox<>();
        for (int i = 1; i <= 12; i++) {
            hoursCombo.getItems().add(i);
        }
        hoursCombo.setValue(4);

        Label vatLabel = new Label("Include VAT:");
        vatLabel.setTextFill(TEXT_COLOR);
        vatLabel.setStyle(LABEL_STYLE);

        ComboBox<String> vatCombo = new ComboBox<>();
        vatCombo.getItems().addAll("Yes", "No");
        vatCombo.setValue("Yes");

        Label resultLabel = new Label("Calculated Revenue:");
        resultLabel.setTextFill(TEXT_COLOR);
        resultLabel.setStyle(LABEL_STYLE);

        Label resultValueLabel = new Label("£0.00");
        resultValueLabel.setTextFill(TEXT_COLOR);
        resultValueLabel.setStyle(LABEL_STYLE);

        Button calculateButton = new Button("Calculate Revenue");
        calculateButton.setOnAction(e -> {
            VenueSpace venue = VenueSpace.valueOf(venueCombo.getValue());
            RevenueCalculator.DayType dayType = RevenueCalculator.DayType.valueOf(dayCombo.getValue());
            BookingType bookingType = BookingType.valueOf(bookingTypeCombo.getValue());
            int hours = hoursCombo.getValue();
            boolean includeVAT = vatCombo.getValue().equals("Yes");

            double revenue = dataManager.calculateRevenue(venue, dayType, bookingType, hours, includeVAT);
            resultValueLabel.setText(String.format("£%.2f", revenue));
        });

        // Add to grid
        grid.add(venueLabel, 0, 0);
        grid.add(venueCombo, 1, 0);

        grid.add(dayLabel, 0, 1);
        grid.add(dayCombo, 1, 1);

        grid.add(bookingTypeLabel, 0, 2);
        grid.add(bookingTypeCombo, 1, 2);

        grid.add(hoursLabel, 0, 3);
        grid.add(hoursCombo, 1, 3);

        grid.add(vatLabel, 0, 4);
        grid.add(vatCombo, 1, 4);

        grid.add(resultLabel, 0, 5);
        grid.add(resultValueLabel, 1, 5);

        grid.add(calculateButton, 1, 6);

        content.getChildren().add(grid);
        return content;
    }

    /**
     * Styles chart text (title, legend) for consistent readability.
     *
     * @param chart the chart to style
     */
    private void styleChartText(Chart chart) {
        // Style chart title
        chart.lookup(".chart-title").setStyle("-fx-text-fill: black; -fx-font-family: 'Cambria';");

        // Style legend items
        Node legend = chart.lookup(".chart-legend");
        if (legend != null) {
            legend.setStyle("-fx-background-color: transparent;");

            for (Node item : legend.lookupAll(".chart-legend-item-text")) {
                item.setStyle("-fx-fill: black; -fx-font-family: 'Cambria';");
            }
        }

        // Style specific chart elements
        if (chart instanceof PieChart) {
            for (Node node : chart.lookupAll(".chart-pie-label")) {
                node.setStyle("-fx-fill: black; -fx-font-family: 'Cambria';");
            }
        }
    }

    /**
     * Styles an axis for clear visibility with black text.
     *
     * @param axis the axis to style
     */
    private void styleAxis(Axis<?> axis) {
        axis.setTickLabelFill(TEXT_COLOR);
        axis.setStyle(AXIS_STYLE);

        // Style axis label
        Node axisLabel = axis.lookup(".axis-label");
        if (axisLabel != null) {
            axisLabel.setStyle("-fx-text-fill: black; -fx-font-family: 'Cambria';");
        }
    }

    /**
     * Applies custom colors to pie chart slices.
     */
    private void applyPieChartColors() {
        if (revenuePieChart == null || revenuePieChart.getData() == null) return;

        int index = 0;
        for (PieChart.Data data : revenuePieChart.getData()) {
            String color = PIE_COLORS[index % PIE_COLORS.length];

            // Use CSS to apply color
            data.getNode().setStyle("-fx-pie-color: " + color + ";");

            index++;
        }
    }

    /**
     * Applies custom colors to bar chart series.
     */
    private void applyBarChartColors() {
        if (revenueBarChart == null || revenueBarChart.getData() == null) return;

        int seriesIndex = 0;
        for (XYChart.Series<String, Number> series : revenueBarChart.getData()) {
            String color = BAR_COLORS[seriesIndex % BAR_COLORS.length];

            // Apply color to each data point in the series
            for (XYChart.Data<String, Number> data : series.getData()) {
                if (data.getNode() != null) {
                    data.getNode().setStyle("-fx-bar-fill: " + color + ";");
                }
            }

            // Update series in legend
            Node legendItem = revenueBarChart.lookup(".chart-legend-item-symbol.default-color" + seriesIndex);
            if (legendItem != null) {
                legendItem.setStyle("-fx-background-color: " + color + ";");
            }

            seriesIndex++;
        }
    }

    /**
     * Applies custom colors to yearly comparison chart.
     */
    private void applyYearChartColors() {
        if (yearComparisonChart == null || yearComparisonChart.getData() == null) return;

        String[] yearColors = {"#2ecc40", "#005355", "#1a2c30"};
        int seriesIndex = 0;

        for (XYChart.Series<String, Number> series : yearComparisonChart.getData()) {
            String color = yearColors[seriesIndex % yearColors.length];

            for (XYChart.Data<String, Number> data : series.getData()) {
                if (data.getNode() != null) {
                    data.getNode().setStyle("-fx-bar-fill: " + color + ";");
                }
            }

            Node legendItem = yearComparisonChart.lookup(".chart-legend-item-symbol.default-color" + seriesIndex);
            if (legendItem != null) {
                legendItem.setStyle("-fx-background-color: " + color + ";");
            }

            seriesIndex++;
        }
    }

    /**
     * Updates all charts, tables, and summary labels based on current filter settings.
     */
    private void updateCharts() {
        // Get filter values
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();
        String selectedVenue = venueSelector.getValue();

        // Get filtered data
        ObservableList<RevenueEntry> filteredData = dataManager.getFilteredData(fromDate, toDate, selectedVenue);
        revenueTable.setItems(filteredData);

        // Get venue revenue data
        Map<String, Double> venueRevenueMap = dataManager.getVenueRevenueMap(filteredData);
        Map<String, Double> venueRoomRateMap = dataManager.getVenueRoomRateMap(filteredData);
        Map<String, Double> venueTicketSalesMap = dataManager.getVenueTicketSalesMap(filteredData);

        // Get sorted maps for better visualization
        Map<String, Double> sortedRevenueMap = dataManager.getSortedVenueMap(venueRevenueMap);
        Map<String, Double> sortedRoomMap = dataManager.getSortedVenueMap(venueRoomRateMap);
        Map<String, Double> sortedTicketMap = dataManager.getSortedVenueMap(venueTicketSalesMap);

        // Update pie chart
        revenuePieChart.setData(dataManager.generatePieChartData(sortedRevenueMap));

        // Clear and update bar chart
        revenueBarChart.getData().clear();
        CategoryAxis xAxis = (CategoryAxis) revenueBarChart.getXAxis();
        xAxis.getCategories().clear();

        // Get sorted venues and add to x-axis
        List<String> sortedVenues = new ArrayList<>(sortedRoomMap.keySet());
        xAxis.getCategories().addAll(sortedVenues);

        // Create series for room hire and ticket sales
        XYChart.Series<String, Number> roomRateSeries = new XYChart.Series<>();
        roomRateSeries.setName("Room Hire");

        XYChart.Series<String, Number> ticketSalesSeries = new XYChart.Series<>();
        ticketSalesSeries.setName("Ticket Sales");

        // Populate series with data
        for (String venue : sortedVenues) {
            roomRateSeries.getData().add(new XYChart.Data<>(venue, sortedRoomMap.get(venue)));
            ticketSalesSeries.getData().add(new XYChart.Data<>(venue, sortedTicketMap.getOrDefault(venue, 0.0)));
        }

        // Add series to bar chart
        revenueBarChart.getData().addAll(roomRateSeries, ticketSalesSeries);

        // Calculate and update summary values
        double totalRevenue = filteredData.stream().mapToDouble(RevenueEntry::getTotalRevenue).sum();
        double roomRevenue = filteredData.stream().mapToDouble(RevenueEntry::getRoomRate).sum();
        double ticketRevenue = filteredData.stream().mapToDouble(RevenueEntry::getTicketSales).sum();

        totalRevenueLabel.setText(String.format("£%.2f", totalRevenue));
        roomHireLabel.setText(String.format("£%.2f", roomRevenue));
        ticketSalesLabel.setText(String.format("£%.2f", ticketRevenue));

        // Update yearly comparison chart
        yearComparisonChart.getData().clear();
        updateYearlyComparisonChart();

        // Apply styling after data is updated
        Platform.runLater(() -> {
            // Apply styles to chart text
            styleChartText(revenuePieChart);
            styleChartText(revenueBarChart);
            styleChartText(yearComparisonChart);

            // Style axes
            styleAxis(xAxis);
            styleAxis((NumberAxis) revenueBarChart.getYAxis());
            styleAxis(yearComparisonChart.getXAxis());
            styleAxis(yearComparisonChart.getYAxis());

            // Apply custom colors
            applyPieChartColors();
            applyBarChartColors();
            applyYearChartColors();
        });
    }

    /**
     * Updates the year-over-year comparison chart with data from the revenue manager.
     */
    private void updateYearlyComparisonChart() {
        Map<String, Map<String, Number>> yearlyData = dataManager.getYearComparisonData();
        yearComparisonChart.getData().addAll(dataManager.generateYearlyComparisonData(yearlyData));
    }

    /**
     * Main method for testing only - in production this will be integrated into the larger application
     */
    public static void main(String[] args) {
        Application.launch(TestUI.class, args);
    }

    /**
     * Test class to launch the UI independently
     */
    public static class TestUI extends Application {
        @Override
        public void start(Stage primaryStage) {
            primaryStage.setTitle("Music Hall Revenue Tracking");
            RevenueTrackingUI revenueUI = new RevenueTrackingUI();
            Scene scene = new Scene(revenueUI, 900, 700);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }
}