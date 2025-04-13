package lancaster.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * A manager for venue revenue data, handling bookings, filtering, and chart-ready aggregates.
 * <p>
 * This class acts as a revenue assistant, providing insights into room hire and ticket sales. It aggregates
 * revenue entries and supports filtering and chart data generation, with data loaded via a pre-defined data load method.
 * </p>
 */
public class RevenueManager {
    private ObservableList<RevenueEntry> revenueData;
    private RevenueCalculator calculator;

    /**
     * Creates a new revenue manager with an empty revenue entry list and a revenue calculator.
     * <p>
     * The revenue data is loaded using a default data load method.
     * </p>
     */
    public RevenueManager() {
        revenueData = FXCollections.observableArrayList();
        calculator = new RevenueCalculator();
        loadSampleData(); // In production, this would be replaced with actual data loading
    }

    /**
     * Gets the full list of revenue entries for all venue bookings.
     *
     * @return the observable list of revenue entries.
     */
    public ObservableList<RevenueEntry> getRevenueData() {
        return revenueData;
    }

    /**
     * Loads revenue data by clearing any existing entries and adding varied entries for halls, rehearsal spaces, and rooms.
     */
    public void loadSampleData() {
        revenueData.clear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Initialize revenue entries for recent dates
        LocalDate now = LocalDate.now();

        // Entries for Main Hall
        revenueData.add(new RevenueEntry("Main Hall", now.minusDays(2).format(formatter), "FULL_DAY", 3800.0, 1950.0));
        revenueData.add(new RevenueEntry("Main Hall", now.minusDays(9).format(formatter), "EVENING", 2200.0, 1620.0));
        revenueData.add(new RevenueEntry("Main Hall", now.minusDays(16).format(formatter), "FULL_DAY", 4200.0, 2300.0));
        revenueData.add(new RevenueEntry("Main Hall", now.minusDays(23).format(formatter), "HOURLY", 975.0, 0.0));

        // Entries for Small Hall
        revenueData.add(new RevenueEntry("Small Hall", now.minusDays(3).format(formatter), "EVENING", 950.0, 480.0));
        revenueData.add(new RevenueEntry("Small Hall", now.minusDays(10).format(formatter), "FULL_DAY", 2200.0, 750.0));
        revenueData.add(new RevenueEntry("Small Hall", now.minusDays(17).format(formatter), "EVENING", 1300.0, 520.0));

        // Entries for Rehearsal Space
        revenueData.add(new RevenueEntry("Rehearsal Space", now.minusDays(4).format(formatter), "FULL_DAY", 240.0, 0.0));
        revenueData.add(new RevenueEntry("Rehearsal Space", now.minusDays(11).format(formatter), "WEEKLY", 1000.0, 0.0));
        revenueData.add(new RevenueEntry("Rehearsal Space", now.minusDays(18).format(formatter), "HOURLY", 180.0, 0.0));

        // Entries for various rooms
        revenueData.add(new RevenueEntry("Green Room", now.minusDays(5).format(formatter), "FULL_DAY", 130.0, 0.0));
        revenueData.add(new RevenueEntry("Brontë Boardroom", now.minusDays(6).format(formatter), "MORNING_AFTERNOON", 120.0, 0.0));
        revenueData.add(new RevenueEntry("Dickens Den", now.minusDays(7).format(formatter), "HOURLY", 30.0, 0.0));
        revenueData.add(new RevenueEntry("Poe Parlor", now.minusDays(12).format(formatter), "WEEKLY", 800.0, 0.0));
        revenueData.add(new RevenueEntry("Globe Room", now.minusDays(19).format(formatter), "FULL_DAY", 250.0, 0.0));
        revenueData.add(new RevenueEntry("Chekhov Chamber", now.minusDays(26).format(formatter), "MORNING_AFTERNOON", 110.0, 0.0));
    }

    /**
     *loading ticket sales data from an external API.
     */
    public void loadTicketSalesData() {
        // Future API integration goes here
    }

    /**
     * Determines if a venue is a smaller "room" rather than a hall or rehearsal space.
     *
     * @param venue the venue name to check.
     * @return {@code true} if the venue is classified as a room, {@code false} otherwise.
     */
    public boolean isRoomVenue(String venue) {
        return venue.equals("Green Room") ||
                venue.equals("Brontë Boardroom") ||
                venue.equals("Dickens Den") ||
                venue.equals("Poe Parlor") ||
                venue.equals("Globe Room") ||
                venue.equals("Chekhov Chamber");
    }

    /**
     * Filters revenue entries by a date range and venue, with special handling for "All Venues" and "Rooms."
     *
     * @param fromDate      the start date of the filter (inclusive).
     * @param toDate        the end date of the filter (inclusive).
     * @param selectedVenue the venue to filter by, or "All Venues" or "Rooms" for broader categories.
     * @return an observable list of revenue entries matching the filter.
     */
    public ObservableList<RevenueEntry> getFilteredData(LocalDate fromDate, LocalDate toDate, String selectedVenue) {
        ObservableList<RevenueEntry> filteredData = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (RevenueEntry entry : revenueData) {
            LocalDate entryDate = LocalDate.parse(entry.getDate(), formatter);

            if ((entryDate.isEqual(fromDate) || entryDate.isAfter(fromDate)) &&
                    (entryDate.isEqual(toDate) || entryDate.isBefore(toDate))) {

                if (selectedVenue.equals("All Venues") || entry.getVenue().equals(selectedVenue) ||
                        (selectedVenue.equals("Rooms") && isRoomVenue(entry.getVenue()))) {
                    filteredData.add(entry);
                }
            }
        }

        return filteredData;
    }

    // Data analysis methods

    /**
     * Sums total revenue for each venue from a filtered set of revenue entries.
     *
     * @param filteredData the revenue entries to analyze.
     * @return a map of venue names to their total revenue.
     */
    public Map<String, Double> getVenueRevenueMap(ObservableList<RevenueEntry> filteredData) {
        Map<String, Double> venueRevenueMap = new HashMap<>();
        for (RevenueEntry entry : filteredData) {
            venueRevenueMap.put(entry.getVenue(),
                    venueRevenueMap.getOrDefault(entry.getVenue(), 0.0) + entry.getTotalRevenue());
        }
        return venueRevenueMap;
    }

    /**
     * Sums total revenue by month from a filtered set of revenue entries.
     *
     * @param filteredData the revenue entries to analyze.
     * @return a map of month strings (YYYY-MM) to their total revenue.
     */
    public Map<String, Double> getMonthlyRevenueMap(ObservableList<RevenueEntry> filteredData) {
        Map<String, Double> monthlyRevenueMap = new HashMap<>();
        for (RevenueEntry entry : filteredData) {
            String month = entry.getDate().substring(0, 7); // Format: YYYY-MM
            monthlyRevenueMap.put(month,
                    monthlyRevenueMap.getOrDefault(month, 0.0) + entry.getTotalRevenue());
        }
        return monthlyRevenueMap;
    }

    /**
     * Sums room hire revenue for each venue from a filtered set of revenue entries.
     *
     * @param filteredData the revenue entries to analyze.
     * @return a map of venue names to their room hire revenue.
     */
    public Map<String, Double> getVenueRoomRateMap(ObservableList<RevenueEntry> filteredData) {
        Map<String, Double> venueRoomRateMap = new HashMap<>();
        for (RevenueEntry entry : filteredData) {
            venueRoomRateMap.put(entry.getVenue(),
                    venueRoomRateMap.getOrDefault(entry.getVenue(), 0.0) + entry.getRoomRate());
        }
        return venueRoomRateMap;
    }

    /**
     * Sums ticket sales revenue for each venue from a filtered set of revenue entries.
     *
     * @param filteredData the revenue entries to analyze.
     * @return a map of venue names to their ticket sales revenue.
     */
    public Map<String, Double> getVenueTicketSalesMap(ObservableList<RevenueEntry> filteredData) {
        Map<String, Double> venueTicketSalesMap = new HashMap<>();
        for (RevenueEntry entry : filteredData) {
            venueTicketSalesMap.put(entry.getVenue(),
                    venueTicketSalesMap.getOrDefault(entry.getVenue(), 0.0) + entry.getTicketSales());
        }
        return venueTicketSalesMap;
    }

    /**
     * Calculates the total room hire revenue across all filtered revenue entries.
     *
     * @param filteredData the revenue entries to sum.
     * @return the total room hire revenue.
     */
    public double calculateTotalRoomRate(ObservableList<RevenueEntry> filteredData) {
        double total = 0;
        for (RevenueEntry entry : filteredData) {
            total += entry.getRoomRate();
        }
        return total;
    }

    /**
     * Calculates the total ticket sales revenue across all filtered revenue entries.
     *
     * @param filteredData the revenue entries to sum.
     * @return the total ticket sales revenue.
     */
    public double calculateTotalTicketSales(ObservableList<RevenueEntry> filteredData) {
        double total = 0;
        for (RevenueEntry entry : filteredData) {
            total += entry.getTicketSales();
        }
        return total;
    }

    /**
     * Provides revenue data for year-over-year comparisons.
     *
     * @return a map of years to monthly revenue totals.
     */
    public Map<String, Map<String, Number>> getYearComparisonData() {
        Map<String, Map<String, Number>> result = new HashMap<>();

        // Pre-defined revenue data for comparison - replace with actual data when available
        Map<String, Number> data2024 = new HashMap<>();
        data2024.put("Jan", 18500);
        data2024.put("Feb", 19200);
        data2024.put("Mar", 22500);
        data2024.put("Apr", 21000);
        data2024.put("May", 23500);
        data2024.put("Jun", 25000);
        data2024.put("Jul", 27000);
        data2024.put("Aug", 24800);
        data2024.put("Sep", 21500);
        data2024.put("Oct", 22300);
        data2024.put("Nov", 23800);
        data2024.put("Dec", 26500);

        Map<String, Number> data2025 = new HashMap<>();
        data2025.put("Jan", 19800);
        data2025.put("Feb", 21500);
        data2025.put("Mar", 24750);

        result.put("2024", data2024);
        result.put("2025", data2025);

        return result;
    }

    /**
     * Sorts a venue revenue map, prioritizing key venues in a specific order.
     * <p>
     * Ensures "Main Hall", "Small Hall", and "Rehearsal Space" appear first, followed by others.
     * </p>
     *
     * @param venueMap the unsorted map of venue revenue.
     * @return a sorted map with preferred venue order.
     */
    public Map<String, Double> getSortedVenueMap(Map<String, Double> venueMap) {
        List<String> venueOrder = Arrays.asList(
                "Main Hall",
                "Small Hall",
                "Rehearsal Space"
        );

        Map<String, Double> sortedMap = new LinkedHashMap<>();

        for (String venue : venueOrder) {
            if (venueMap.containsKey(venue)) {
                sortedMap.put(venue, venueMap.get(venue));
            }
        }

        for (Map.Entry<String, Double> entry : venueMap.entrySet()) {
            if (!sortedMap.containsKey(entry.getKey())) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }
        }
        return sortedMap;
    }

    /**
     * Estimates revenue for a potential booking using the revenue calculator.
     *
     * @param venue       the venue space to book.
     * @param dayType     the day type (e.g., weekday or weekend).
     * @param bookingType the booking duration (e.g., hourly, full day).
     * @param hours       the number of hours if hourly booking.
     * @param includeVAT  whether to include VAT in the estimate.
     * @return the estimated total revenue.
     */
    public double calculateRevenue(RevenueCalculator.VenueSpace venue, RevenueCalculator.DayType dayType, RevenueCalculator.BookingType bookingType, int hours, boolean includeVAT) {
        return calculator.calculateTotalRevenue(venue, dayType, bookingType, hours, includeVAT);
    }

    // Chart data generation methods

    /**
     * Generates pie chart data from a venue revenue map.
     *
     * @param venueRevenueMap the map of venues to their revenue.
     * @return an observable list of pie chart data entries.
     */
    public ObservableList<PieChart.Data> generatePieChartData(Map<String, Double> venueRevenueMap) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Double> entry : venueRevenueMap.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        return pieChartData;
    }

    /**
     * Generates bar chart data comparing room hire and ticket sales by venue.
     *
     * @param venueRoomRateMap    the map of venues to room hire revenue.
     * @param venueTicketSalesMap the map of venues to ticket sales revenue.
     * @return a list of bar chart series for room hire and ticket sales.
     */
    public List<XYChart.Series<String, Number>> generateBarChartData(Map<String, Double> venueRoomRateMap,
                                                                     Map<String, Double> venueTicketSalesMap) {
        List<XYChart.Series<String, Number>> result = new ArrayList<>();

        XYChart.Series<String, Number> roomHireSeries = new XYChart.Series<>();
        roomHireSeries.setName("Room Hire");

        XYChart.Series<String, Number> ticketSalesSeries = new XYChart.Series<>();
        ticketSalesSeries.setName("Ticket Sales");

        for (String venue : venueRoomRateMap.keySet()) {
            roomHireSeries.getData().add(new XYChart.Data<>(venue, venueRoomRateMap.getOrDefault(venue, 0.0)));
            ticketSalesSeries.getData().add(new XYChart.Data<>(venue, venueTicketSalesMap.getOrDefault(venue, 0.0)));
        }

        result.add(roomHireSeries);
        result.add(ticketSalesSeries);

        return result;
    }

    /**
     * Generates bar chart data for year-over-year revenue comparisons.
     *
     * @param yearlyData the map of years to monthly revenue data.
     * @return a list of bar chart series, one per year.
     */
    public List<XYChart.Series<String, Number>> generateYearlyComparisonData(Map<String, Map<String, Number>> yearlyData) {
        List<XYChart.Series<String, Number>> result = new ArrayList<>();

        for (Map.Entry<String, Map<String, Number>> yearEntry : yearlyData.entrySet()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(yearEntry.getKey());

            // Add data points for each month
            for (Map.Entry<String, Number> monthEntry : yearEntry.getValue().entrySet()) {
                series.getData().add(new XYChart.Data<>(monthEntry.getKey(), monthEntry.getValue()));
            }

            result.add(series);
        }

        return result;
    }
}
