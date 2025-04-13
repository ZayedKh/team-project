package lancaster.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.*;

/**
 * Controller class for managing the seating layout of the Theater.
 * <p>
 * This controller is responsible for creating and displaying the theater seating layout,
 * handling seat selection and booking actions, applying zoom functionality using scroll events,
 * and updating seat statuses (such as AVAILABLE, RESERVED, or UNAVAILABLE).
 * </p>
 */
public class TheaterSeatingController implements Initializable {

    @FXML
    private ScrollPane scrollPane; // ScrollPane to allow scrolling and zooming of the seating layout.

    @FXML
    private BorderPane mainBorderPane; // Main container (if used for additional layout management).

    @FXML
    private Pane seatingContainer; // Container that holds all seat nodes and related layout elements.

    @FXML
    private Label eventLabel; // Label to display current event information.

    @FXML
    private Button bookButton; // Button that confirms booking of selected seats.

    private double scaleFactor = 1.0; // Current scaling factor for zoom operations.

    private static final double SEAT_WIDTH = 20; // Width of each seat in pixels.
    private static final double SEAT_HEIGHT = 20; // Height of each seat in pixels.
    private static final double SEAT_SPACING = 5; // Spacing between seats in pixels.

    // Map that holds the current status of each seat identified by its seat ID.
    private Map<String, SeatStatus> seatStatusMap = new HashMap<>();

    // Set of seat IDs that are currently selected for booking.
    private Set<String> selectedSeats = new HashSet<>();

    /**
     * Enum representing the possible status values for a seat.
     * <p>
     * Each status contains its own background color, border color, and a descriptive text.
     * </p>
     */
    public enum SeatStatus {
        AVAILABLE("#E8F5E9", "#4CAF50", "Available"),
        RESERVED("#FFF8E1", "#FFA000", "Reserved"),
        UNAVAILABLE("#FFEBEE", "#D32F2F", "Unavailable");

        private final String backgroundColor;
        private final String borderColor;
        private final String description;

        SeatStatus(String backgroundColor, String borderColor, String description) {
            this.backgroundColor = backgroundColor;
            this.borderColor = borderColor;
            this.description = description;
        }

        /**
         * Returns the background color associated with this seat status.
         *
         * @return a String representing the background color in hex format.
         */
        public String getBackgroundColor() { return backgroundColor; }

        /**
         * Returns the border color associated with this seat status.
         *
         * @return a String representing the border color in hex format.
         */
        public String getBorderColor() { return borderColor; }

        /**
         * Returns the descriptive text for this seat status.
         *
         * @return a String describing the seat status.
         */
        public String getDescription() { return description; }
    }

    /**
     * Initializes the TheaterSeatingController after the FXML elements have been injected.
     * <p>
     * This method sets up the scroll pane, creates the seating layout, adds additional layout elements
     * (such as stage area), initializes seat statuses, and updates the seating display and booking button state.
     * </p>
     *
     * @param location  The location used to resolve relative paths for the root object, or {@code null} if unknown.
     * @param resources The resource bundle used to localize the root object, or {@code null} if not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        createSeatingLayout();
        addStageArea();
        initializeRandomSeatStatuses();
        updateSeatingDisplay();
        // Update the book button's enabled state based on seat selections.
        updateBookButtonState();
    }

    /**
     * Creates the seating layout for the theater.
     * <p>
     * This method clears the seating container and generates the seating layout by creating balcony areas,
     * a stalls area, and adding section labels.
     * </p>
     */
    private void createSeatingLayout() {
        seatingContainer.getChildren().clear();
        double centerX = 500; // Center position used to horizontally center the seating layout.
        createBalconyAreas(centerX);
        createStallsArea(centerX);
        createSectionLabel("BALCONY", centerX - 50, 10);
        createSectionLabel("STALLS", centerX - 50, 200);
    }

    /**
     * Creates the three balcony areas: left, right, and center.
     *
     * @param centerX The horizontal center coordinate used for layout calculations.
     */
    private void createBalconyAreas(double centerX) {
        createLeftBalconyArea();
        createRightBalconyArea();
        createCenterBalconyArea(centerX);
    }

    /**
     * Creates the center balcony area with rows of seats.
     *
     * @param centerX The horizontal center coordinate for centering the balcony rows.
     */
    private void createCenterBalconyArea(double centerX) {
        String[] rowLabels = {"CC", "BB", "AA"};
        int[] startSeatNums = {1, 6, 21};
        int[] endSeatNums = {8, 23, 33};
        double centerBalconyStartY = 40;

        for (int rowIndex = 0; rowIndex < rowLabels.length; rowIndex++) {
            String row = rowLabels[rowIndex];
            int startSeat = startSeatNums[rowIndex];
            int endSeat = endSeatNums[rowIndex];
            int numSeats = endSeat - startSeat + 1;

            double totalWidthRow = numSeats * (SEAT_WIDTH + SEAT_SPACING) - SEAT_SPACING;
            double rowStartX = centerX - (totalWidthRow / 2);
            double y = centerBalconyStartY + rowIndex * (SEAT_HEIGHT + SEAT_SPACING);

            for (int seatNum = startSeat; seatNum <= endSeat; seatNum++) {
                String seatId = row + seatNum;
                double x = rowStartX + (seatNum - startSeat) * (SEAT_WIDTH + SEAT_SPACING);
                StackPane seat = createSeat(seatId, x, y, SeatStatus.AVAILABLE);
                seatingContainer.getChildren().add(seat);
            }
        }
    }

    /**
     * Creates the left balcony area.
     * <p>
     * This area contains two sets of seats with identifiers starting with "BB" and "AA".
     * </p>
     */
    private void createLeftBalconyArea() {
        double balconyStartX = 50;
        double balconyStartY = 40;

        int bbSeatCount = 5;
        double bbX = balconyStartX;
        for (int i = bbSeatCount; i >= 1; i--) {
            double y = balconyStartY + (bbSeatCount - i) * (SEAT_HEIGHT + SEAT_SPACING);
            String seatId = "BB" + i;
            StackPane seat = createSeat(seatId, bbX, y, SeatStatus.AVAILABLE);
            seatingContainer.getChildren().add(seat);
        }

        int aaSeatCount = 20;
        double aaX = balconyStartX + (SEAT_WIDTH + SEAT_SPACING) * 2;
        for (int i = aaSeatCount; i >= 1; i--) {
            double y = balconyStartY + (aaSeatCount - i) * (SEAT_HEIGHT + SEAT_SPACING);
            String seatId = "AA" + i;
            StackPane seat = createSeat(seatId, aaX, y, SeatStatus.AVAILABLE);
            seatingContainer.getChildren().add(seat);
        }
    }

    /**
     * Creates the right balcony area.
     * <p>
     * This method creates two sets of seats for the right balcony with identifiers starting with "AA" and "BB".
     * </p>
     */
    private void createRightBalconyArea() {
        double balconyStartX = 830;
        double balconyStartY = 40;

        int aaSeatCount = 20;
        double aaX = balconyStartX;
        for (int i = 0; i < aaSeatCount; i++) {
            double y = balconyStartY + i * (SEAT_HEIGHT + SEAT_SPACING);
            int seatNumber = 34 + i;
            String seatId = "AA" + seatNumber;
            StackPane seat = createSeat(seatId, aaX, y, SeatStatus.AVAILABLE);
            seatingContainer.getChildren().add(seat);
        }

        int bbSeatCount = 5;
        double bbX = balconyStartX + (SEAT_WIDTH + SEAT_SPACING) * 2;
        for (int i = 0; i < bbSeatCount; i++) {
            double y = balconyStartY + i * (SEAT_HEIGHT + SEAT_SPACING);
            int seatNumber = 24 + i;
            String seatId = "BB" + seatNumber;
            StackPane seat = createSeat(seatId, bbX, y, SeatStatus.AVAILABLE);
            seatingContainer.getChildren().add(seat);
        }
    }

    /**
     * Creates the stalls area for the theater seating.
     * <p>
     * This method iterates through defined stall rows, determines the number of seats per row,
     * and generates seat nodes accordingly.
     * </p>
     *
     * @param centerX The horizontal center coordinate to center each row.
     */
    private void createStallsArea(double centerX) {
        String[] stallRows = {"Q", "P", "O", "N", "M", "L", "K", "J", "I", "H", "G", "F", "E", "D", "C", "B", "A"};
        double startY = 220;

        for (int rowIndex = 0; rowIndex < stallRows.length; rowIndex++) {
            String row = stallRows[rowIndex];
            int seatsPerRow;
            switch (row) {
                case "Q":
                    seatsPerRow = 10;
                    break;
                case "P":
                    seatsPerRow = 11;
                    break;
                case "O":
                    seatsPerRow = 20;
                    break;
                case "M":
                    seatsPerRow = 16;
                    break;
                default:
                    seatsPerRow = 19;
                    break;
            }

            double totalWidthRow = seatsPerRow * (SEAT_WIDTH + SEAT_SPACING) - SEAT_SPACING;
            double startX = centerX - (totalWidthRow / 2);
            double y = startY + rowIndex * (SEAT_HEIGHT + SEAT_SPACING);

            for (int seatNum = 1; seatNum <= seatsPerRow; seatNum++) {
                String seatId = row + seatNum;
                double x = startX + (seatNum - 1) * (SEAT_WIDTH + SEAT_SPACING);
                StackPane seat = createSeat(seatId, x, y, SeatStatus.AVAILABLE);
                seatingContainer.getChildren().add(seat);
            }
        }
    }

    /**
     * Adds a stage area below the seating layout.
     * <p>
     * The stage area is created as a StackPane with a label "STAGE" and is positioned based on the maximum y-coordinate of the seats.
     * </p>
     */
    private void addStageArea() {
        double maxY = 0;
        for (Node node : seatingContainer.getChildren()) {
            if (node instanceof StackPane) {
                double seatY = node.getLayoutY() + SEAT_HEIGHT;
                if (seatY > maxY) maxY = seatY;
            }
        }
        StackPane stageArea = new StackPane();
        stageArea.setPrefSize(1000, 25);
        stageArea.setLayoutX(0);
        stageArea.setLayoutY(maxY + 5);
        stageArea.setStyle("-fx-background-color: #CCCCCC; -fx-background-radius: 5 5 0 0;");
        Label stageLabel = new Label("STAGE");
        stageLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        stageArea.getChildren().add(stageLabel);
        seatingContainer.getChildren().add(stageArea);
    }

    /**
     * Creates a seat node with the given identifier, coordinates, and status.
     * <p>
     * Each seat is represented as a StackPane with a label and tooltip.
     * A click handler is assigned to allow selection if the seat is available.
     * </p>
     *
     * @param seatId The unique identifier for the seat.
     * @param x The x-coordinate for the seat placement.
     * @param y The y-coordinate for the seat placement.
     * @param status The initial status for the seat.
     * @return A StackPane representing the seat.
     */
    private StackPane createSeat(String seatId, double x, double y, SeatStatus status) {
        StackPane seat = new StackPane();
        seat.setId("seat-" + seatId);
        seat.setLayoutX(x);
        seat.setLayoutY(y);
        seat.setPrefSize(SEAT_WIDTH, SEAT_HEIGHT);
        seat.getStyleClass().add("seat");
        String style = "-fx-background-color: " + status.getBackgroundColor() + ";" +
                "-fx-border-color: " + status.getBorderColor() + ";" +
                "-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-width: 1;";
        seat.setStyle(style);

        Label seatLabel = new Label(seatId);
        seatLabel.setFont(Font.font("System", FontWeight.NORMAL, 6));
        seatLabel.setTextFill(Color.BLACK);
        seat.getChildren().add(seatLabel);

        seatStatusMap.put(seatId, status);
        seat.setOnMouseClicked(event -> handleSeatClick(seatId, seat));
        Tooltip tooltip = new Tooltip(seatId + " - " + status.getDescription());
        Tooltip.install(seat, tooltip);

        return seat;
    }

    /**
     * Handles a mouse click event on a seat.
     * <p>
     * Toggles the selection state of the seat if it is available, updates its appearance, and adjusts the booking button state.
     * </p>
     *
     * @param seatId The unique identifier of the clicked seat.
     * @param seat The StackPane representing the seat.
     */
    private void handleSeatClick(String seatId, StackPane seat) {
        if (seatStatusMap.get(seatId) == SeatStatus.AVAILABLE) {
            if (selectedSeats.contains(seatId)) {
                selectedSeats.remove(seatId);
                updateSeatAppearance(seat, seatId, false);
            } else {
                selectedSeats.add(seatId);
                updateSeatAppearance(seat, seatId, true);
            }
            updateBookButtonState();
        }
    }

    /**
     * Updates the visual appearance of a seat based on its selection state.
     *
     * @param seat The StackPane representing the seat.
     * @param seatId The unique identifier of the seat.
     * @param isSelected True if the seat is selected; false otherwise.
     */
    private void updateSeatAppearance(StackPane seat, String seatId, boolean isSelected) {
        SeatStatus status = seatStatusMap.get(seatId);
        String style = "-fx-background-color: " + status.getBackgroundColor() + ";" +
                "-fx-border-color: " + status.getBorderColor() + ";" +
                "-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-width: " + (isSelected ? "2" : "1") + ";";
        if (isSelected) style += "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);";
        seat.setStyle(style);
    }

    /**
     * Iterates through all seat nodes to update their appearance based on current status and selection.
     */
    private void updateSeatingDisplay() {
        seatingContainer.getChildren().forEach(node -> {
            if (node instanceof StackPane && node.getId() != null && node.getId().startsWith("seat-")) {
                String seatId = node.getId().substring(5);
                boolean isSelected = selectedSeats.contains(seatId);
                updateSeatAppearance((StackPane) node, seatId, isSelected);
                SeatStatus status = seatStatusMap.get(seatId);
                Tooltip.install(node, new Tooltip(seatId + " - " + status.getDescription()));
            }
        });
    }

    /**
     * Updates the enabled state of the booking button.
     * <p>
     * The booking button is enabled only if at least one seat is selected.
     * </p>
     */
    private void updateBookButtonState() {
        bookButton.setDisable(selectedSeats.isEmpty());
    }

    /**
     * Initializes seat statuses for all seats.
     * <p>
     * For demonstration purposes, each seat is assigned a status:
     * AVAILABLE, RESERVED, or UNAVAILABLE.
     * </p>
     */
    private void initializeRandomSeatStatuses() {
        Random random = new Random();
        for (String seatId : seatStatusMap.keySet()) {
            int rand = random.nextInt(10);
            seatStatusMap.put(seatId, rand < 6 ? SeatStatus.AVAILABLE : rand < 8 ? SeatStatus.RESERVED : SeatStatus.UNAVAILABLE);
        }
    }

    /**
     * Handles the booking action when the booking button is clicked.
     * <p>
     * Displays a confirmation dialog with the selected seat IDs, then marks those seats as RESERVED,
     * clears the current selection, and updates the seating display.
     * </p>
     */
    @FXML
    private void handleBookSeats() {
        if (!selectedSeats.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Booking Confirmation");
            alert.setHeaderText("Seat Booking");
            String seatsText = "You have selected the following seats:\n" + String.join(", ", selectedSeats);
            alert.setContentText(seatsText);
            alert.showAndWait();
            for (String seatId : selectedSeats) seatStatusMap.put(seatId, SeatStatus.RESERVED);
            selectedSeats.clear();
            updateSeatingDisplay();
            updateBookButtonState();
        }
    }

    /**
     * Handles scroll events to implement zooming on the seating layout.
     * <p>
     * When the Control key is pressed while scrolling, the seating container is scaled (zoomed in or out)
     * based on the scroll delta, within a defined range.
     * </p>
     *
     * @param event The ScrollEvent triggered by the user.
     */
    private void handleScroll(ScrollEvent event) {
        if (event.isControlDown()) {
            double deltaY = event.getDeltaY();
            scaleFactor = Math.max(0.5, Math.min(3.0, deltaY > 0 ? scaleFactor * 1.1 : scaleFactor / 1.1));
            seatingContainer.setScaleX(scaleFactor);
            seatingContainer.setScaleY(scaleFactor);
            event.consume();
        }
    }

    /**
     * Updates the status of a specific seat and refreshes the seating display.
     *
     * @param seatId The identifier of the seat to update.
     * @param status The new status for the seat.
     */
    public void updateSeatStatus(String seatId, SeatStatus status) {
        if (seatStatusMap.containsKey(seatId)) {
            seatStatusMap.put(seatId, status);
            updateSeatingDisplay();
        }
    }

    /**
     * Creates and adds a section label to the seating container.
     * <p>
     * Section labels (e.g., "BALCONY" or "STALLS") help denote different areas within the seating layout.
     * </p>
     *
     * @param text The text to display in the label.
     * @param x The x-coordinate for label placement.
     * @param y The y-coordinate for label placement.
     */
    private void createSectionLabel(String text, double x, double y) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.BOLD, 14));
        label.setTextFill(Color.BLACK);
        label.setLayoutX(x);
        label.setLayoutY(y);
        seatingContainer.getChildren().add(label);
    }

    /**
     * Sets the event information to be displayed on the seating layout.
     * <p>
     * This method updates the eventLabel with the current event name and date.
     * </p>
     *
     * @param eventName The name of the current event.
     * @param eventDate The date of the current event.
     */
    public void setEventInfo(String eventName, Date eventDate) {
        eventLabel.setText("Current Event: " + eventName + " - " + eventDate);
    }
}
