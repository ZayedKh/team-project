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
 * Controller class for managing the theater seating layout and booking functionality in a JavaFX application.
 */
public class TheaterSeatingController implements Initializable {

    @FXML private ScrollPane scrollPane;         // Scroll pane containing the seating layout
    @FXML private BorderPane mainBorderPane;     // Main container for the layout
    @FXML private Pane seatingContainer;         // Container for seating elements
    @FXML private Label eventLabel;             // Label displaying current event information
    @FXML private Button bookButton;            // Button to confirm seat booking

    private double scaleFactor = 1.0;            // Zoom scale factor for seating display
    private static final double SEAT_WIDTH = 20;  // Width of each seat
    private static final double SEAT_HEIGHT = 20; // Height of each seat
    private static final double SEAT_SPACING = 5; // Spacing between seats

    private Map<String, SeatStatus> seatStatusMap = new HashMap<>(); // Tracks status of all seats
    private Set<String> selectedSeats = new HashSet<>();            // Set of currently selected seat IDs

    /**
     * Enum representing the possible status states of a seat.
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

        public String getBackgroundColor() { return backgroundColor; }
        public String getBorderColor() { return borderColor; }
        public String getDescription() { return description; }
    }

    /**
     * Initializes the controller after its root element has been processed.
     * @param location The location used to resolve relative paths for the root object
     * @param resources The resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        createSeatingLayout();
        addStageArea();
        initializeRandomSeatStatuses();
        updateSeatingDisplay();
    }

    /**
     * Creates the overall seating layout including balcony and stalls areas.
     */
    private void createSeatingLayout() {
        seatingContainer.getChildren().clear();
        double centerX = 500;  // Center point for layout alignment
        createBalconyAreas(centerX);
        createStallsArea(centerX);
        createSectionLabel("BALCONY", centerX - 50, 10);
        createSectionLabel("STALLS", centerX - 50, 200);
    }

    /**
     * Creates all balcony seating areas.
     * @param centerX The horizontal center point for alignment
     */
    private void createBalconyAreas(double centerX) {
        createLeftBalconyArea();
        createRightBalconyArea();
        createCenterBalconyArea(centerX);
    }

    /**
     * Creates the center balcony seating area.
     * @param centerX The horizontal center point for alignment
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

            // Create seats for the row
            for (int seatNum = startSeat; seatNum <= endSeat; seatNum++) {
                String seatId = row + seatNum;
                double x = rowStartX + (seatNum - startSeat) * (SEAT_WIDTH + SEAT_SPACING);
                StackPane seat = createSeat(seatId, x, y, SeatStatus.AVAILABLE);
                seatingContainer.getChildren().add(seat);
            }
        }
    }

    /**
     * Creates the left balcony seating area.
     */
    private void createLeftBalconyArea() {
        double balconyStartX = 50;
        double balconyStartY = 40;

        // BB row seats
        int bbSeatCount = 5;
        double bbX = balconyStartX;
        for (int i = bbSeatCount; i >= 1; i--) {
            double y = balconyStartY + (bbSeatCount - i) * (SEAT_HEIGHT + SEAT_SPACING);
            String seatId = "BB" + i;
            StackPane seat = createSeat(seatId, bbX, y, SeatStatus.AVAILABLE);
            seatingContainer.getChildren().add(seat);
        }

        // AA row seats
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
     * Creates the right balcony seating area.
     */
    private void createRightBalconyArea() {
        double balconyStartX = 830;
        double balconyStartY = 40;

        // AA row seats
        int aaSeatCount = 20;
        double aaX = balconyStartX;
        for (int i = 0; i < aaSeatCount; i++) {
            double y = balconyStartY + i * (SEAT_HEIGHT + SEAT_SPACING);
            int seatNumber = 34 + i;
            String seatId = "AA" + seatNumber;
            StackPane seat = createSeat(seatId, aaX, y, SeatStatus.AVAILABLE);
            seatingContainer.getChildren().add(seat);
        }

        // BB row seats
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
     * Creates the stalls seating area.
     * @param centerX The horizontal center point for alignment
     */
    private void createStallsArea(double centerX) {
        String[] stallRows = {"Q", "P", "O", "N", "M", "L", "K", "J", "I", "H", "G", "F", "E", "D", "C", "B", "A"};
        double startY = 220;

        for (int rowIndex = 0; rowIndex < stallRows.length; rowIndex++) {
            String row = stallRows[rowIndex];

            // Determine seats per row based on row letter
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

            // Create seats for the row
            for (int seatNum = 1; seatNum <= seatsPerRow; seatNum++) {
                String seatId = row + seatNum;
                double x = startX + (seatNum - 1) * (SEAT_WIDTH + SEAT_SPACING);
                StackPane seat = createSeat(seatId, x, y, SeatStatus.AVAILABLE);
                seatingContainer.getChildren().add(seat);
            }
        }
    }

    /**
     * Adds the stage area to the layout below the seating.
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
     * Creates a visual representation of a seat.
     * @param seatId Unique identifier for the seat
     * @param x X-coordinate position
     * @param y Y-coordinate position
     * @param status Initial status of the seat
     * @return StackPane representing the seat
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
     * Handles mouse click events on seats for selection/deselection.
     * @param seatId The ID of the clicked seat
     * @param seat The StackPane representing the seat
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
     * Updates the visual appearance of a seat based on selection state.
     * @param seat The StackPane representing the seat
     * @param seatId The ID of the seat
     * @param isSelected Whether the seat is currently selected
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
     * Updates the display of all seats based on their current status and selection state.
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
     * Updates the enabled/disabled state of the book button based on seat selection.
     */
    private void updateBookButtonState() {
        bookButton.setDisable(selectedSeats.isEmpty());
    }

    /**
     * Initializes seat statuses.
     */
    private void initializeRandomSeatStatuses() {
        Random random = new Random();
        for (String seatId : seatStatusMap.keySet()) {
            int rand = random.nextInt(10);
            seatStatusMap.put(seatId, rand < 6 ? SeatStatus.AVAILABLE : rand < 8 ? SeatStatus.RESERVED : SeatStatus.UNAVAILABLE);
        }
    }

    /**
     * Handles the booking of selected seats and shows confirmation dialog.
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
     * Handles scroll events for zooming the seating display.
     * @param event The scroll event
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
     * Updates the status of a specific seat.
     * @param seatId The ID of the seat to update
     * @param status The new status to apply
     */
    public void updateSeatStatus(String seatId, SeatStatus status) {
        if (seatStatusMap.containsKey(seatId)) {
            seatStatusMap.put(seatId, status);
            updateSeatingDisplay();
        }
    }

    /**
     * Creates a section label for the seating layout.
     * @param text The text to display
     * @param x X-coordinate position
     * @param y Y-coordinate position
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
     * Sets the event information to display on the label.
     * @param eventName The name of the event
     * @param eventDate The date of the event
     */
    public void setEventInfo(String eventName, Date eventDate) {
        eventLabel.setText("Current Event: " + eventName + " - " + eventDate);
    }
}