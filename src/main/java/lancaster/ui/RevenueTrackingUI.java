package lancaster.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lancaster.model.RevenueCalculator.*;
import lancaster.model.RevenueEntry;
import lancaster.model.RevenueManager;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * A graphical dashboard for tracking and analyzing venue revenue.
 * Features tabs for an overview, detailed reports, year-over-year comparisons, and revenue calculations,
 * with filters and charts to provide clear financial insights for venue managers.
 */
public class RevenueTrackingUI extends BorderPane {

    private RevenueManager dataManager;

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

    private static final Color TEXT_COLOR = Color.WHITE;
    private static final String[] PIE_COLORS = {"#2ECC40", "#005355", "#00a561", "#1a2c30", "#4472C4"};
    private static final String[] BAR_COLORS = {"#2ECC40", "#005355"};

    private static final String CHART_TEXT_STYLE = "-fx-text-fill: white; -fx-font-family: 'Cambria';";
    private static final String AXIS_STYLE = "-fx-tick-label-fill: white; -fx-font-family: 'Cambria';";
    private static final String LABEL_STYLE = "-fx-text-fill: white; -fx-font-family: 'Cambria';";

    /**
     * Constructs the revenue tracking dashboard, initializing the data manager and UI components.
     */
    public RevenueTrackingUI() {
        // Initialize models
        dataManager = new RevenueManager();

        // Set up UI
        setupUI();
        updateCharts();
    }

    /**
     * Sets up the dashboard layout with a filter bar at the top and a tabbed interface in the center.
     */
    private void setupUI() {
        this.setPadding(new Insets(10));

        HBox filterBar = createFilterBar();
        this.setTop(filterBar);

        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-text-fill: white;");

        Tab overviewTab = new Tab("Revenue Overview");
        Tab detailedTab = new Tab("Detailed Reports");
        Tab comparisonTab = new Tab("Year Comparison");
        Tab calculationTab = new Tab("Calculate Revenue");

        overviewTab.setClosable(false);
        detailedTab.setClosable(false);
        comparisonTab.setClosable(false);
        calculationTab.setClosable(false);

        // Set up content for each tab
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

        GridPane summaryGrid = new GridPane();
        summaryGrid.setHgap(20);
        summaryGrid.setVgap(10);
        summaryGrid.setPadding(new Insets(10));

        Label fromLabel = new Label("From:");
        fromDatePicker = new DatePicker(LocalDate.now().minusMonths(1));

        Label toLabel = new Label("To:");
        toDatePicker = new DatePicker(LocalDate.now());

        Label venueLabel = new Label("Venue:");
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

        GridPane summaryGrid = new GridPane();
        summaryGrid.setHgap(20);
        summaryGrid.setVgap(10);
        summaryGrid.setPadding(new Insets(10));

        addStyledLabel(summaryGrid, "Total Revenue:", totalRevenueLabel = new Label("£0.00"), 0);
        addStyledLabel(summaryGrid, "Room Hire Revenue:", roomHireLabel = new Label("£0.00"), 1);
        addStyledLabel(summaryGrid, "Ticket Sales Revenue:", ticketSalesLabel = new Label("£0.00"), 2);

        revenuePieChart = new PieChart();
        revenuePieChart.setTitle("Revenue Breakdown by Venue");
        styleChartText(revenuePieChart);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        revenueBarChart = new BarChart<>(xAxis, yAxis);
        revenueBarChart.setTitle("Revenue by Type");
        styleChartText(revenueBarChart);

        xAxis.setLabel("Venue");
        yAxis.setLabel("Revenue (£)");
        styleAxis(xAxis);
        styleAxis(yAxis);

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
        valueLabel.setTextFill(TEXT_COLOR);
        grid.add(label, 0, row);
        grid.add(valueLabel, 1, row);
    }

    /**
     * Styles chart text (title, legend) for consistent readability.
     *
     * @param chart the chart to style
     */
    private void styleChartText(Chart chart) {
        chart.setStyle("-fx-text-fill: white;");
        chart.lookup(".chart-title").setStyle("-fx-text-fill: white; -fx-font-family: 'Cambria';");

        Node legend = chart.lookup(".chart-legend");
        if (legend != null) {
            legend.setStyle("-fx-background-color: transparent;");

            for (Node item : legend.lookupAll(".chart-legend-item")) {
                item.setStyle("-fx-text-fill: white;");
            }

            for (Node text : legend.lookupAll(".chart-legend-item-text")) {
                text.setStyle("-fx-fill: white; -fx-font-family: 'Cambria';");
            }
        }

        if (chart instanceof PieChart) {
            for (Node node : chart.lookupAll(".chart-pie-label")) {
                node.setStyle("-fx-fill: white; -fx-font-family: 'Cambria';");
            }
        }
        if (chart instanceof XYChart) {
            for (Node node : chart.lookupAll(".chart-bar-label")) {
                node.setStyle("-fx-text-fill: white; -fx-font-family: 'Cambria';");
            }

            for (Node node : chart.lookupAll(".data")) {
                node.setStyle("-fx-text-fill: white;");
            }

            for (Node node : chart.lookupAll(".text")) {
                node.setStyle("-fx-fill: white;");
            }
        }
    }

    /**
     * Styles an axis for clear visibility with white text.
     *
     * @param axis the axis to style
     */
    private void styleAxis(Axis<?> axis) {
        axis.setTickLabelFill(TEXT_COLOR);
        axis.setStyle(AXIS_STYLE);

        Node axisLabel = axis.lookup(".axis-label");
        if (axisLabel != null) {
            axisLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Cambria';");
        }
    }

    /**
     * Creates the "Detailed Reports" tab with a table of all revenue entries.
     *
     * @return a VBox with the detailed content
     */
    private VBox createDetailedContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(10));

        revenueTable = new TableView<>();

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

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yearComparisonChart = new BarChart<>(xAxis, yAxis);

        yearComparisonChart.setTitle("Year-over-Year Revenue Comparison");
        xAxis.setLabel("Month");
        yAxis.setLabel("Revenue (£)");

        yearComparisonChart.setPrefHeight(500);

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

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        // Create all labels with white text
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
            DayType dayType = DayType.valueOf(dayCombo.getValue());
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
     * Applies consistent colors to charts for visual clarity.
     */
    private void applyChartColors() {
        if (revenuePieChart != null && revenuePieChart.getData() != null) {
            for (int i = 0; i < revenuePieChart.getData().size(); i++) {
                PieChart.Data data = revenuePieChart.getData().get(i);
                String color = PIE_COLORS[i % PIE_COLORS.length];

                data.getNode().setStyle("-fx-pie-color: " + color + ";");

                Node legendItem = revenuePieChart.lookup(".chart-legend-item-symbol.default-color" + i);
                if (legendItem != null) {
                    legendItem.setStyle("-fx-background-color: " + color + ";");
                }
            }
        }

        if (revenueBarChart != null && revenueBarChart.getData() != null) {
            int seriesIndex = 0;
            for (XYChart.Series<String, Number> series : revenueBarChart.getData()) {
                String color = BAR_COLORS[seriesIndex % BAR_COLORS.length];

                for (XYChart.Data<String, Number> data : series.getData()) {
                    if (data.getNode() != null) {
                        data.getNode().setStyle("-fx-bar-fill: " + color + ";");
                    }
                }

                Node legendItem = revenueBarChart.lookup(".chart-legend-item-symbol.default-color" + seriesIndex);
                if (legendItem != null) {
                    legendItem.setStyle("-fx-background-color: " + color + ";");
                }

                seriesIndex++;
            }
        }

        if (yearComparisonChart != null && yearComparisonChart.getData() != null) {
            int seriesIndex = 0;
            String[] yearColors = {"#2ecc40", "#005355", "#1a2c30"};

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
    }

    /**
     * Updates all charts, tables, and summary labels based on current filter settings.
     */
    private void updateCharts() {
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();
        String selectedVenue = venueSelector.getValue();

        ObservableList<RevenueEntry> filteredData = dataManager.getFilteredData(fromDate, toDate, selectedVenue);

        revenueTable.setItems(filteredData);

        Map<String, Double> venueRevenueMap = dataManager.getVenueRevenueMap(filteredData);
        Map<String, Double> venueRoomRateMap = dataManager.getVenueRoomRateMap(filteredData);
        Map<String, Double> venueTicketSalesMap = dataManager.getVenueTicketSalesMap(filteredData);

        Map<String, Double> sortedRevenueMap = dataManager.getSortedVenueMap(venueRevenueMap);
        Map<String, Double> sortedRoomMap = dataManager.getSortedVenueMap(venueRoomRateMap);
        Map<String, Double> sortedTicketMap = dataManager.getSortedVenueMap(venueTicketSalesMap);

        revenuePieChart.setData(dataManager.generatePieChartData(sortedRevenueMap));
        Map<String, String> venueColors = new HashMap<>();
        venueColors.put("Main Hall", "#4472C4");
        venueColors.put("Small Hall", "#ED7D31");
        venueColors.put("Rehearsal Space", "#A5A5A5");

        for (int i = 0; i < revenuePieChart.getData().size(); i++) {
            PieChart.Data data = revenuePieChart.getData().get(i);
            data.getNode().setStyle("-fx-pie-color: " + PIE_COLORS[i % PIE_COLORS.length] + ";");
        }
        revenueBarChart.getData().clear();

        XYChart.Series<String, Number> roomRateSeries = new XYChart.Series<>();
        roomRateSeries.setName("Room Hire");

        XYChart.Series<String, Number> ticketSalesSeries = new XYChart.Series<>();
        ticketSalesSeries.setName("Ticket Sales");

        for (String venue : sortedRoomMap.keySet()) {
            roomRateSeries.getData().add(new XYChart.Data<>(venue, sortedRoomMap.get(venue)));
        }

        for (String venue : sortedTicketMap.keySet()) {
            ticketSalesSeries.getData().add(new XYChart.Data<>(venue, sortedTicketMap.get(venue)));
        }

        revenueBarChart.getData().addAll(roomRateSeries, ticketSalesSeries);

        // --- Summary Labels ---
        double totalRevenue = filteredData.stream().mapToDouble(RevenueEntry::getTotalRevenue).sum();
        double roomRevenue = filteredData.stream().mapToDouble(RevenueEntry::getRoomRate).sum();
        double ticketRevenue = filteredData.stream().mapToDouble(RevenueEntry::getTicketSales).sum();

        totalRevenueLabel.setText(String.format("£%.2f", totalRevenue));
        roomHireLabel.setText(String.format("£%.2f", roomRevenue));
        ticketSalesLabel.setText(String.format("£%.2f", ticketRevenue));

        // Optional: update yearComparisonChart if you want
        yearComparisonChart.getData().clear();
        yearComparisonChart.setTitle("Year-over-Year Revenue Comparison");

        // Update yearly comparison chart
        updateYearlyComparisonChart();

        Platform.runLater(() -> {
            styleChartText(revenuePieChart);
            styleChartText(revenueBarChart);
            styleChartText(yearComparisonChart);

            // Style the axes
            for (Node node : revenueBarChart.lookupAll(".axis")) {
                if (node instanceof Axis) {
                    styleAxis((Axis<?>) node);
                }
            }

            for (Node node : yearComparisonChart.lookupAll(".axis")) {
                if (node instanceof Axis) {
                    styleAxis((Axis<?>) node);
                }
            }

            // Apply and ensure consistent colors
            applyChartColors();

            // Additional styling for data labels if needed
            for (Node n : revenueBarChart.lookupAll(".data0.chart-bar")) {
                n.setStyle("-fx-bar-fill: " + BAR_COLORS[0] + ";");
            }

            for (Node n : revenueBarChart.lookupAll(".data1.chart-bar")) {
                n.setStyle("-fx-bar-fill: " + BAR_COLORS[1] + ";");
            }

            revenuePieChart.applyCss();
            revenueBarChart.applyCss();
            yearComparisonChart.applyCss();
        });
    }

    /**
     * Updates the year-over-year comparison chart with data from the revenue manager.
     */
    private void updateYearlyComparisonChart() {
        yearComparisonChart.getData().clear();

        Map<String, Map<String, Number>> yearlyData = dataManager.getYearComparisonData();
        yearComparisonChart.getData().addAll(dataManager.generateYearlyComparisonData(yearlyData));

        String[] yearColors = {"#2ecc40", "#005355", "#1a2c30"};
        int seriesIndex = 0;

        for (XYChart.Series<String, Number> series : yearComparisonChart.getData()) {
            String color = yearColors[seriesIndex % yearColors.length];
            for (XYChart.Data<String, Number> data : series.getData()) {
                data.getNode().setStyle("-fx-bar-fill: " + color + ";");
            }
            seriesIndex++;
        }
    }
}